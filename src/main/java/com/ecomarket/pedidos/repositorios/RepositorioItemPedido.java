package com.ecomarket.pedidos.repositorios;

import com.ecomarket.pedidos.models.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository 
public interface RepositorioItemPedido extends JpaRepository<ItemPedido, Long> {

}
