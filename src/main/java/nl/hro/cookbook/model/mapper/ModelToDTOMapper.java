package nl.hro.cookbook.model.mapper;

public interface ModelToDTOMapper<M, D> {
    M toModel(D dto);
    D toDTO(M model);
}