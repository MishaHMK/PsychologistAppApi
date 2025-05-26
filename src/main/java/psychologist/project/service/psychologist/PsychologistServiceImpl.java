package psychologist.project.service.psychologist;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psychologist.project.config.ImageConfig;
import psychologist.project.dto.psychologist.CreatePsychologistDto;
import psychologist.project.dto.psychologist.PagedPsychologistDto;
import psychologist.project.dto.psychologist.PsychologistDto;
import psychologist.project.dto.psychologist.PsychologistFilterDto;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;
import psychologist.project.mapper.PsychologistMapper;
import psychologist.project.model.Psychologist;
import psychologist.project.model.Speciality;
import psychologist.project.model.User;
import psychologist.project.repository.psychologist.PsychologistRepository;
import psychologist.project.repository.psychologist.PsychologistSpecification;
import psychologist.project.repository.psychologist.SpecialityRepository;
import psychologist.project.repository.user.UserRepository;
import psychologist.project.security.SecurityUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class PsychologistServiceImpl implements PsychologistService {
    private final PsychologistRepository psychologistRepository;
    private final PsychologistMapper psychologistMapper;
    private final SpecialityRepository specialityRepository;
    private final UserRepository userRepository;
    private final ImageConfig imageConfig;

    @Override
    public PsychologistWithDetailsDto getPsychologist(Long id) {
        return psychologistMapper.toDetailedDto(
                psychologistRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Psychologist with id "
                                + id + " not found"))
                );
    }

    @Override
    public PsychologistDto save(CreatePsychologistDto createDto) {
        Psychologist toCreate = psychologistMapper.toEntity(createDto);
        if (toCreate.getImageUrl() == null
                || toCreate.getImageUrl().isEmpty()) {
            switch (toCreate.getGender()) {
                case MALE:
                    toCreate.setImageUrl(imageConfig.getDefaultMaleImg());
                    break;
                case FEMALE:
                    toCreate.setImageUrl(imageConfig.getDefaultFemaleImg());
                    break;
                default:
                    toCreate.setImageUrl(imageConfig.getDefaultOtherImg());
                    break;
            }
        }
        int experience = toCreate.getExperience();
        if (experience > 0 && experience <= 2) {
            toCreate.setSessionPrice(BigDecimal.valueOf(199.99));
        } else if (experience > 2 && experience <= 5) {
            toCreate.setSessionPrice(BigDecimal.valueOf(299.99));
        } else if (experience > 5) {
            toCreate.setSessionPrice(BigDecimal.valueOf(499.99));
        }

        PsychologistDto dto = psychologistMapper.toDto(psychologistRepository.save(toCreate));
        return dto.setSpeciality(findSpecialityById(createDto.getSpecialityId()));
    }

    @Override
    public PsychologistWithDetailsDto likePsychologist(Long id) {
        PsychologistWithDetailsDto psychologist = getPsychologist(id);
        User user = userRepository.getById(SecurityUtil.getLoggedInUserId());
        Set<Psychologist> likedPsychologists = user.getLikedPsychologists();
        Long targetId = psychologist.getId();
        Optional<Psychologist> toRemove = likedPsychologists.stream()
                .filter(p -> p.getId().equals(targetId))
                .findFirst();
        if (toRemove.isPresent()) {
            likedPsychologists.remove(toRemove.get());
            psychologist.setIsLiked(false);
        } else {
            likedPsychologists.add(psychologistMapper.detailsToEntity(psychologist));
            psychologist.setIsLiked(true);
        }
        userRepository.save(user);
        return psychologist;
    }

    @Override
    public List<PsychologistWithDetailsDto> getAllPsychologists(Pageable pageable) {
        return psychologistRepository.findAll(pageable)
                .stream()
                .map(psychologistMapper::toDetailedDto)
                .toList();
    }

    @Override
    public PagedPsychologistDto getAllLikedPsychologists(Pageable pageable) {
        Long id = SecurityUtil.getLoggedInUserId();
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
        List<PsychologistWithDetailsDto> fullList = user.getLikedPsychologists()
                .stream()
                .map(p -> {
                    PsychologistWithDetailsDto dto = psychologistMapper.toDetailedDto(p);
                    dto.setIsLiked(true);
                    return dto;
                })
                .toList();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<PsychologistWithDetailsDto> pageContent;

        if (startItem >= fullList.size()) {
            pageContent = Collections.emptyList();
        } else {
            int endIndex = Math.min(startItem + pageSize, fullList.size());
            pageContent = fullList.subList(startItem, endIndex);
        }

        Page<PsychologistWithDetailsDto> page
                = new PageImpl<>(pageContent, pageable, fullList.size());

        return new PagedPsychologistDto()
                .setPsychologists(page.getContent())
                .setCount((int) page.getTotalElements())
                .setPageNumber(page.getNumber())
                .setPageSize(page.getSize())
                .setTotalPages(page.getTotalPages());
    }

    @Override
    public void delete(Long id) {
        psychologistRepository.deleteById(id);
    }

    @Override
    public PagedPsychologistDto search(PsychologistFilterDto filterDto,
                                  Pageable pageable) {
        Specification<Psychologist> spec = setUpSpecification(filterDto);
        Page<Psychologist> page = psychologistRepository.findAll(spec, pageable);
        Set<Long> likedIds = Set.of();
        Optional<User> user = userRepository
                .findById(SecurityUtil.getValidUserIdIfAuthenticated());
        if (user.isPresent()) {
            likedIds = user.get().getLikedPsychologists()
                   .stream()
                   .map(Psychologist::getId)
                   .collect(Collectors.toSet());
        }
        Set<Long> finalLikedIds = likedIds;
        List<PsychologistWithDetailsDto> psychologistList =
                page.getContent()
                        .stream()
                        .map(p -> {
                            PsychologistWithDetailsDto dto = psychologistMapper.toDetailedDto(p);
                            dto.setIsLiked(finalLikedIds.contains(p.getId()));
                            return dto;
                        })
                        .toList();

        PagedPsychologistDto pagedBookingDto = new PagedPsychologistDto()
                .setPsychologists(psychologistList)
                .setCount((int) page.getTotalElements())
                .setPageNumber(pageable.getPageNumber())
                .setPageSize(pageable.getPageSize());

        return pagedBookingDto.setTotalPages(
                (int) Math.ceil((double) pagedBookingDto.getCount()
                        / pagedBookingDto.getPageSize()));
    }

    private Speciality findSpecialityById(Long id) {
        return specialityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Speciality with id " + id + " not found")
        );
    }

    private Specification<Psychologist> setUpSpecification(
            PsychologistFilterDto filterDto
    ) {
        Specification<Psychologist> spec = Specification.where(null);

        String firstName = filterDto.getFirstName();
        if (firstName != null) {
            spec = spec.and(PsychologistSpecification
                    .hasFirstName(firstName));
        }

        String gender = filterDto.getGender();
        if (gender != null) {
            spec = spec.and(PsychologistSpecification
                    .hasGender(gender));
        }

        Long specialityId = filterDto.getSpecialityId();
        if (specialityId != null) {
            spec = spec.and(PsychologistSpecification
                    .hasSpecialityId(specialityId));
        }

        BigDecimal minPrice = filterDto.getMinPrice();
        if (minPrice != null) {
            spec = spec.and(PsychologistSpecification
                    .hasMinPrice(minPrice));
        }

        BigDecimal maxPrice = filterDto.getMaxPrice();
        if (maxPrice != null) {
            spec = spec.and(PsychologistSpecification
                    .hasMaxPrice(maxPrice));
        }

        Long[] concernIds = filterDto.getConcernIds();
        if (concernIds != null && concernIds.length > 0) {
            spec = spec.and(PsychologistSpecification
                    .hasConcernIds(concernIds));
        }

        Long[] approachIds = filterDto.getApproachIds();
        if (approachIds != null && approachIds.length > 0) {
            spec = spec.and(PsychologistSpecification
                    .hasApproachIds(approachIds));
        }

        return spec;
    }
}
