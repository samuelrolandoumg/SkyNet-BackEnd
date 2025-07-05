/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import com.cloudinary.Cloudinary;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import services.CloudinarySvc;

/**
 *
 * @author Samuel
 */
@Service
@Slf4j
public class CloudinarySvcImpl implements CloudinarySvc {

    private final Cloudinary cloudinary;

    public CloudinarySvcImpl() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "du8d9j1tt");
        config.put("api_key", "439628621875642");
        config.put("api_secret", "BOsEQgh8B0_1L9SV_BEL1vq332w");

        cloudinary = new Cloudinary(config);
    }

    @Override
    public String subirImagenBase64(String base64, String folder) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64.split(",")[1]); // quitar el "data:image/..." si viene

            Map<String, Object> options = new HashMap<>();
            options.put("folder", folder);
            options.put("resource_type", "image");

            Map uploadResult = cloudinary.uploader().upload(decoded, options);
            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            log.error("Error al subir imagen a Cloudinary", e);
            throw new RuntimeException("Error al subir imagen a Cloudinary: " + e.getMessage());
        }
    }
}
