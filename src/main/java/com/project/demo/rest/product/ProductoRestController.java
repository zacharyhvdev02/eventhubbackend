package com.project.demo.rest.product;

import com.project.demo.logic.entity.category.Categoria;
import com.project.demo.logic.entity.category.CategoriaRepository;
import com.project.demo.logic.entity.order.Order;
import com.project.demo.logic.entity.product.Producto;
import com.project.demo.logic.entity.product.ProductoRepository;
import com.project.demo.logic.http.GlobalResponseHandler;
import com.project.demo.logic.http.Meta;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Get all products
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllProductos(
            @RequestParam(defaultValue = "0") int page, // Número de página, por defecto 0
            @RequestParam(defaultValue = "10") int size, // Tamaño de página, por defecto 10
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Producto> productsPage = productoRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(productsPage.getTotalPages());
        meta.setTotalElements(productsPage.getTotalElements());
        meta.setPageNumber(productsPage.getNumber());
        meta.setPageSize(productsPage.getSize());

        return new GlobalResponseHandler().handleResponse(
                "Orders retrieved successfully",
                productsPage.getContent(), // El contenido de la página
                HttpStatus.OK,
                meta
        );

    }

    // Add a new product
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Producto addProducto(@RequestBody Producto producto) {
        Optional<Categoria> categoria = categoriaRepository.findById(producto.getCategory().getId());

        if (categoria.isEmpty()) {
            throw new EntityNotFoundException("Category not found. Please make sure to select an existing category");
        }

        return productoRepository.save(producto);
    }

    // Get a product by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
    public Producto getProductoById(@PathVariable Long id) {
        return productoRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    // Update an existing product
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        Optional<Producto> productoOptional = productoRepository.findById(id);



        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.setName(productoDetails.getName());
            producto.setDescription(productoDetails.getDescription());
            producto.setPrice(productoDetails.getPrice());
            producto.setStock(productoDetails.getStock());
            producto.setCategory(productoDetails.getCategory());
            Producto updatedProducto = productoRepository.save(producto);
            return ResponseEntity.ok(updatedProducto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete a product
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER-ADMIN-ROLE')")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
