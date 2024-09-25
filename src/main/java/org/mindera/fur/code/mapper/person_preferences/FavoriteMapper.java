package org.mindera.fur.code.mapper.person_preferences;

import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mindera.fur.code.dto.person_preferences.FavoriteDTO;
import org.mindera.fur.code.mapper.PersonMapper;
import org.mindera.fur.code.mapper.pet.PetMapper;
import org.mindera.fur.code.model.person_preferences.Favorite;

import java.util.List;

@Schema(description = "The favorite mapper")
@Mapper(componentModel = "spring") //, uses = {PetMapper.class, PersonMapper.class})
public interface FavoriteMapper {

    FavoriteMapper INSTANCE = Mappers.getMapper(FavoriteMapper.class);

    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "pet.id", target = "petId")
    FavoriteDTO toDto(Favorite favorite);

    @InheritInverseConfiguration
    Favorite toModel(FavoriteDTO favoriteDTO);

    List<FavoriteDTO> toDtoList(List<Favorite> favorites);
}
