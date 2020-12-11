//package nl.hro.cookbook.controller;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.Optional;
//import java.util.zip.DataFormatException;
//import java.util.zip.Deflater;
//import java.util.zip.Inflater;
//
//import nl.hro.cookbook.model.domain.RecipeImage;
//import nl.hro.cookbook.repository.ImageRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.ResponseEntity.BodyBuilder;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//@RestController
//@CrossOrigin(origins = "http://localhost:4200")
//@RequestMapping(path = "image")
//public class ImageUploadController {
//
//    @Autowired
//    ImageRepository imageRepository;
//
//    @PostMapping("/upload")
//    public BodyBuilder uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
//        System.out.println("Original RecipeImage Byte Size - " + file.getBytes().length);
//        RecipeImage img = new RecipeImage(file.getOriginalFilename(), file.getContentType(),
//                compressBytes(file.getBytes()));
//        imageRepository.save(img);
//        return ResponseEntity.status(HttpStatus.OK);
//    }
//
//    @GetMapping(path = { "/get/{imageName}" })
//    public RecipeImage getImage(@PathVariable("imageName") String imageName) throws IOException {
//        final Optional<RecipeImage> retrievedImage = imageRepository.findByName(imageName);
//        RecipeImage img = new RecipeImage(retrievedImage.get().getName(), retrievedImage.get().getType(),
//                decompressBytes(retrievedImage.get().getPicByte()));
//        return img;
//    }
//
//    public static byte[] compressBytes(byte[] data) {
//        Deflater deflater = new Deflater();
//        deflater.setInput(data);
//        deflater.finish();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        while (!deflater.finished()) {
//            int count = deflater.deflate(buffer);
//            outputStream.write(buffer, 0, count);
//        }
//        try {
//            outputStream.close();
//        } catch (IOException e) {
//
//        }
//        System.out.println("Compressed RecipeImage Byte Size - " + outputStream.toByteArray().length);
//        return outputStream.toByteArray();
//    }
//
//    // uncompress the image bytes before returning it to the angular application
//    public static byte[] decompressBytes(byte[] data) {
//        Inflater inflater = new Inflater();
//        inflater.setInput(data);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        try {
//            while (!inflater.finished()) {
//                int count = inflater.inflate(buffer);
//                outputStream.write(buffer, 0, count);
//            }
//            outputStream.close();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } catch (DataFormatException e) {
//        }
//        return outputStream.toByteArray();
//    }
//}
//
//
