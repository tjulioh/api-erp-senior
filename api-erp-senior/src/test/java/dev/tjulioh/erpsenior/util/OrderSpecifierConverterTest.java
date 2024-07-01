package dev.tjulioh.erpsenior.util;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import dev.tjulioh.erpsenior.domain.Pedido;
import dev.tjulioh.erpsenior.domain.PedidoItem;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

class OrderSpecifierConverterTest {

    private final OrderSpecifierConverter orderSpecifierConverter;

    OrderSpecifierConverterTest() {
        this.orderSpecifierConverter = new OrderSpecifierConverter(Pedido.class);
    }

    @Test
    void testListEntityFilter() {
        OrderSpecifier<?>[] predicate = orderSpecifierConverter.toOrderSpecifier("itens.quantidade desc");

        OrderSpecifier<?>[] predicateExpected = new OrderSpecifier[]{
                new PathBuilder<>(Pedido.class, "pedido").getList("itens", PedidoItem.class).any().getNumber("quantidade", Integer.class).desc()
        };
        assert Arrays.equals(predicate, predicateExpected);
    }

    @Test
    void testSimpleFilter() {
        OrderSpecifier<?>[] order = orderSpecifierConverter.toOrderSpecifier("descricao asc");

        OrderSpecifier<?>[] orderExpected = new OrderSpecifier[]{
                new PathBuilder<>(Pedido.class, "pedido").getString("descricao").asc()
        };

        assert Arrays.equals(order, orderExpected);
    }

    @Test
    void testComposedFilter() {
        OrderSpecifier<?>[] order = orderSpecifierConverter.toOrderSpecifier("itens.quantidade asc,id desc,descricao asc");

        OrderSpecifier<?>[] orderExpected = new OrderSpecifier[]{
                new PathBuilder<>(Pedido.class, "pedido").getList("itens", PedidoItem.class).any().getNumber("quantidade", Integer.class).asc(),
                new PathBuilder<>(Pedido.class, "pedido").getComparable("id", UUID.class).desc(),
                new PathBuilder<>(Pedido.class, "pedido").getString("descricao").asc()
        };

        assert Arrays.equals(order, orderExpected);
    }

    @Test
    void testInvalidFilterString() {
        try {
            orderSpecifierConverter.toOrderSpecifier("valor 2");
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Field does not exist valor");
        }
    }

}