package com.project.demo.logic.entity.models;

import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ModelsSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final ModelRepository modelRepository;


    public ModelsSeeder(
            ModelRepository modelRepository
    ) {
        this.modelRepository = modelRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // this.createModels();
    }

    private void createModels() {
        Model model = new Model();
        model.setModel_path("table.glb");
        modelRepository.save(model);
    }
}
