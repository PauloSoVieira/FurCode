package org.mindera.fur.code.aspect.roleauth;

import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.infra.security.TokenService;
import org.mindera.fur.code.messages.pet.PetMessages;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.model.ShelterPersonRoles;
import org.mindera.fur.code.model.pet.Pet;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterPersonRolesRepository;
import org.mindera.fur.code.repository.pet.PetRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class RoleAuthAspect {

    private final TokenService tokenService;
    private final PersonRepository personRepository;
    private final ShelterPersonRolesRepository shelterPersonRolesRepository;
    private final PetRepository petRepository;

    public RoleAuthAspect(TokenService tokenService,
                          PersonRepository personRepository,
                          ShelterPersonRolesRepository shelterPersonRolesRepository,
                          PetRepository petRepository) {
        this.tokenService = tokenService;
        this.personRepository = personRepository;
        this.shelterPersonRolesRepository = shelterPersonRolesRepository;
        this.petRepository = petRepository;
    }

    /**
     * Authorize role.
     *
     * @param joinPoint The join point.
     * @param requiresRole The requires role.
     * @return The object.
     * @throws Throwable The throwable.
     */
    @Around("@annotation(requiresRole)")
    public Object authorizeRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("Request attributes not found");
        }

        String authHeader = attributes.getRequest().getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new PersonException("UNAUTHORIZED");
        }

        String jwtToken = authHeader.substring(7);
        String email = tokenService.validateToken(jwtToken);

        Person person = personRepository.findByEmail(email);
        if (person == null) {
            throw new PersonException("PERSON_NOT_FOUND");
        }

        Long shelterId;
        if (requiresRole.isPetOperation()) {
            shelterId = extractShelterIdFromPet(joinPoint, requiresRole);
        } else {
            shelterId = extractShelterId(joinPoint, requiresRole);
        }

        ShelterPersonRoles personRole = shelterPersonRolesRepository
                .findByPersonIdAndShelterId(person.getId(), shelterId)
                .orElseThrow(() -> new PersonException("PERSON_NOT_ASSOCIATED_WITH_SHELTER"));

        if (personRole.getRole().ordinal() > requiresRole.value().ordinal()) {
            throw new PersonException("INSUFFICIENT_PERMISSIONS");
        }

        return joinPoint.proceed();
    }

    /**
     * Extract shelter id.
     *
     * @param joinPoint The join point.
     * @param requiresRole The requires role.
     * @return The shelter id.
     */
    private Long extractShelterId(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) {
        if (!requiresRole.shelterIdField().isEmpty()) {
            // Extract from request body
            Object requestBody = findRequestBody(joinPoint.getArgs());
            if (requestBody == null) {
                throw new IllegalArgumentException("Request body not found");
            }
            return extractShelterIdFromField(requestBody, requiresRole.shelterIdField());
        } else if (requiresRole.shelterIdParam() >= 0) {
            // Extract from method parameter
            Object[] args = joinPoint.getArgs();
            if (requiresRole.shelterIdParam() >= args.length) {
                throw new IllegalArgumentException("Invalid shelter ID parameter index");
            }
            return (Long) args[requiresRole.shelterIdParam()];
        } else {
            throw new IllegalArgumentException("No shelter ID source specified");
        }
    }

    private Long extractShelterIdFromPet(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) {
        Object[] args = joinPoint.getArgs();
        if (requiresRole.petIdParam() >= args.length) {
            throw new IllegalArgumentException("Invalid pet ID parameter index");
        }
        Long petId = (Long) args[requiresRole.petIdParam()];
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException(PetMessages.PET_NOT_FOUND + petId));
        return pet.getShelter().getId();
    }

    /**
     * Find request body.
     *
     * @param args The args.
     * @return The request body.
     */
    private Object findRequestBody(Object[] args) {
        for (Object arg : args) {
            if (arg != null && !(arg instanceof String)) {
                return arg;
            }
        }
        return null;
    }

    /**
     * Extract shelter id from field.
     *
     * @param requestBody The request body.
     * @param fieldName The field name.
     * @return The shelter id.
     */
    private Long extractShelterIdFromField(Object requestBody, String fieldName) {
        try {
            Field field = requestBody.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (Long) field.get(requestBody);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to extract shelterId from request body", e);
        }
    }
}