package com.customify.service;

import com.customify.dto.product.ProductResponse;
import com.customify.model.Product;
import com.customify.model.Role;
import com.customify.model.User;
import com.customify.repository.ProductRepository;
import com.customify.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProductService productService;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Preparamos datos falsos para usar en los tests antes de cada prueba
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("fabricante_test");
        testUser.setEmail("test@empresa.com");
        testUser.setPassword("password");
        testUser.setRole(Role.FABRICANTE);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Silla de Escritorio");
        testProduct.setDescription("Silla ergonómica de prueba");
        testProduct.setBasePrice(new BigDecimal("150.00"));
        testProduct.setOwner(testUser);
        testProduct.setModelPath("silla.glb");
        testProduct.setActive(true);
    }

    @Test
    void testGetAllProductsByUsername_Success() {
        // 1. Arrange (Preparar el escenario)
        when(userRepository.findByUsername("fabricante_test")).thenReturn(Optional.of(testUser));
        when(productRepository.findByOwner(testUser)).thenReturn(List.of(testProduct));

        // 2. Act (Ejecutar el método a probar)
        List<ProductResponse> responses = productService.getAllProductsByUsername("fabricante_test");

        // 3. Assert (Comprobar los resultados)
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Silla de Escritorio", responses.get(0).getName());
        assertEquals(new BigDecimal("150.00"), responses.get(0).getBasePrice());

        // Verificamos que el repositorio fue llamado exactamente una vez
        verify(productRepository, times(1)).findByOwner(testUser);
    }

    @Test
    void testCreateProduct_WithoutFile_Success() {
        // Arrange
        when(userRepository.findByUsername("fabricante_test")).thenReturn(Optional.of(testUser));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        ProductResponse response = productService.createProduct(
                "fabricante_test",
                "Silla de Escritorio",
                "Silla ergonómica de prueba",
                new BigDecimal("150.00"),
                null // Sin archivo para simplificar el test
        );

        // Assert
        assertNotNull(response);
        assertEquals("Silla de Escritorio", response.getName());
        assertEquals(1L, response.getId());

        // Verificamos que no se llamó al servicio de archivos porque le pasamos null
        verify(fileStorageService, never()).storeFile(any());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById_UserNotOwner_ThrowsException() {
        // Arrange: Creamos un usuario "intruso"
        User hacker = new User();
        hacker.setUsername("hacker_user");

        // El producto pertenece a "fabricante_test", no a "hacker_user"
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act & Assert: Comprobamos que salta la excepción de seguridad
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductById(1L, "hacker_user");
        });

        assertEquals("No tienes permiso para acceder a este producto", exception.getMessage());
    }
}