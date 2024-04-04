package sit.int204.classicmodelsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int204.classicmodelsservice.services.FileService;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    FileService fileService;

    @GetMapping("/test")
    public ResponseEntity<Object> testPropertiesMapping() {
        return ResponseEntity.ok(fileService.getFileStorageLocation() + " has been create !!!");
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public  ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileService.loadFileAsResource(filename);
        String fileName = file.getFilename();
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        switch (extension) {
            case ".pdf": return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(file);
            case ".png": return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(file);
            case ".jpeg": return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
            case ".gif": return ResponseEntity.ok().contentType(MediaType.IMAGE_GIF).body(file);
            default: return ResponseEntity.ok().contentType(MediaType.ALL).body(file);
        }
    }

    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<Object> fileDelete(@PathVariable String filename) {
        fileService.restore(filename);
        return ResponseEntity.ok("You successfully delete " + filename);
    }

    @PostMapping("")
    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file) {
        fileService.store(file);
        return ResponseEntity.ok("You successfully upload " + file.getOriginalFilename());
    }
}
