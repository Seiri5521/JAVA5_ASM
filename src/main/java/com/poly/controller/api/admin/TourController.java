package com.poly.controller.api.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dto.ResponseDTO;
import com.poly.dto.TourDTO;
import com.poly.dto.ToutStartAddDTO;
import com.poly.model.Tour;
import com.poly.model.TourStart;
import com.poly.repository.TourStartRepository;
import com.poly.services.ImageService;
import com.poly.services.TourService;
import com.poly.services.UserService;
import com.poly.utilites.DateUtils;
import com.poly.utilites.FileUploadUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/tour")
public class TourController {

    @Autowired
    private TourService tourService;

    @Autowired
    private TourStartRepository tourStartRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAllTour")
    public ResponseDTO getAllTour(@RequestParam(value="ten_tour",required = false) String ten_tour,
                                  @RequestParam(value="gia_tour_from",required = false) Long gia_tour_from,
                                  @RequestParam(value="gia_tour_to",required = false) Long gia_tour_to,
                                  @RequestParam(value="ngay_khoi_hanh",required = false) Date ngay_khoi_hanh,
                                  @RequestParam(value="loai_tour",required = false) Integer loai_tour,
                                  @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "pageIndex") Integer pageIndex
                                                                                        ) {
        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        Page<TourDTO> page = this.tourService.findAllTourAdmin(ten_tour,gia_tour_from,gia_tour_to,ngay_khoi_hanh,loai_tour, PageRequest.of(pageIndex,pageSize));

        return new ResponseDTO("Thành công",page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseDTO getOneTour(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        TourDTO tour = this.tourService.findTourById(id);

        if(tour!=null) {
            return new ResponseDTO("Thành công",tour);
        }
        return new ResponseDTO("Thất bại" ,null);
    }

//    @PostMapping("/test-up-anh")
//    public ResponseDTO testUpAnh(@RequestParam("image")MultipartFile image) {
//
//        if(!this.userService.checkAdminLogin()) {
//            return new ResponseDTO("Không có quyền truy cập",null);
//        }
//
//        String uploadDir = "TourIMG/src/main/resources/static/public/img";
//
//        try {
//            // Lưu ảnh vào thư mục "upload"
//            String fileName = UUID.randomUUID().toString()+ image.getOriginalFilename();
//            FileUploadUtil.saveFile(uploadDir, fileName, image);
//
//            return new ResponseDTO("Thành công",fileName);
//        } catch (IOException  e) {
//            // Xử lý exception
//            log.info("Lỗi upload file: {}",e.getMessage());
//        }
//        return new ResponseDTO("Thêm thất bại",null);
//    }

    @PostMapping("/add/image")
    public ResponseDTO createTourImage(@RequestParam("image")MultipartFile image) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        String uploadDir = "src/main/resources/static/public/img";

        Tour tour = tourService.findFirstByOrderByIdDesc();

        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tour.setAnh_tour(fileName);
            return new ResponseDTO("Thành công",this.tourService.saveTour(tour));
        } catch (IOException  e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }catch (Exception e) {
            log.error("Lỗi không xác định: {}", e.getMessage(), e);
            return new ResponseDTO("Lỗi không xác định: " + e.getMessage(), null);
        }
        return new ResponseDTO("Thêm thất bại",null);
    }

    @PostMapping("/add")
    public ResponseDTO createTour(@RequestBody TourDTO tourDTO) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        String[] dataGet = tourDTO.getDiem_den().split("/");
        tourDTO.setDiem_den(dataGet[0]);
        tourDTO.setNgay_khoi_hanh(DateUtils.convertStringToDate(dataGet[1]));
        tourDTO.setNgay_ket_thuc(DateUtils.convertStringToDate(dataGet[2]));

        Tour tour = this.tourService.addTour(tourDTO);
        if(tour!=null) {
            return new ResponseDTO("Thành công",tour);
        }
        return new ResponseDTO("Thêm thất bại",null);

    }


    @PutMapping("/update/image/{id}")
    public ResponseDTO updateTourImage(@PathVariable("id") Long id,@RequestParam("image") MultipartFile image) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        String uploadDir = "src/main/resources/static/public/img";

        TourDTO tourDTO = this.tourService.findTourById(id);
        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tourDTO.setAnh_tour(fileName);
            Tour updateTour = this.tourService.updateTour(tourDTO,id);
            if(updateTour!=null) {
                return new ResponseDTO("Thành công",updateTour);
            }

        } catch (IOException  e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }
        return new ResponseDTO("Update thất bại",null);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO updateTour(@PathVariable("id") Long id,@RequestBody TourDTO tourDTO) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        String[] dataGet = tourDTO.getDiem_den().split("/");
        tourDTO.setDiem_den(dataGet[0]);
        tourDTO.setNgay_khoi_hanh(DateUtils.convertStringToDate(dataGet[1]));
        tourDTO.setNgay_ket_thuc(DateUtils.convertStringToDate(dataGet[2]));
        Tour updateTour = this.tourService.updateTour(tourDTO,id);
        if(updateTour!=null) {
            return new ResponseDTO("Thành công",updateTour);
        }

        return new ResponseDTO("Update thất bại",null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteTour(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        if(this.tourService.findTourById(id)!=null) {
            if(this.tourService.deleteTour(id)) {
                return new ResponseDTO("Xóa thành công",null);
            }

        }
        return new ResponseDTO("Xóa thất bại",null);
    }

    @GetMapping("/getAllImageOfTour/{id}")
    public ResponseDTO getAllImageOfTour(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        return new ResponseDTO("Thành công",this.imageService.findByTourId(id));
    }

    @PostMapping("/add-image/{id}")
    public ResponseEntity<ResponseDTO> addImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile image) {

        // Kiểm tra quyền truy cập
        if (!this.userService.checkAdminLogin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseDTO("Không có quyền truy cập", null));
        }

        // Thư mục lưu trữ ảnh
        String uploadDir = "TourIMG/public/img";

        // Tạo thư mục nếu chưa tồn tại
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        try {
            // Tạo tên file ngẫu nhiên và lưu file
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Kiểm tra tồn tại của tour
            if (this.tourService.findTourById(id) != null) {
                // Lưu ảnh vào tour và trả về phản hồi
                Object savedImage = this.imageService.addToTour(id, fileName);
                return ResponseEntity.ok(new ResponseDTO("Thêm thành công", savedImage));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO("Tour không tồn tại", null));
            }
        } catch (IOException e) {
            // Log lỗi chi tiết và trả về phản hồi lỗi
            log.error("Lỗi upload file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("Lỗi khi xử lý file", null));
        } catch (Exception e) {
            // Log lỗi không mong muốn khác
            log.error("Lỗi không xác định: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("Lỗi không xác định", null));
        }
    }

    
    @GetMapping("/StartDate/{id}")
    public ResponseDTO getAllStartDate(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        return new ResponseDTO("Thành công",this.tourStartRepository.getDateStartByTourId(id));
    }


    @DeleteMapping("/StartDate/delete/{id}")
    public ResponseDTO deleteStartDate(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        this.tourStartRepository.deleteById(id);
        return new ResponseDTO("Xóa thành công",null);
    }

    @DeleteMapping("/TourImage/delete/{id}")
    public ResponseDTO deleteTourImage(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        this.imageService.deleteById(id);

        return new ResponseDTO("Xóa ảnh thành công",null);
    }

    @PostMapping("/add-date/{id}")
    public ResponseDTO addStartDate(@PathVariable("id") Long id , @RequestBody ToutStartAddDTO toutStartAddDTO) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseDTO("Không có quyền truy cập",null);
        }

        Date ngay_khoi_hanh = DateUtils.convertStringToDate(toutStartAddDTO.getNgay_khoi_hanh());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ngay_khoi_hanh);
        calendar.add(Calendar.DAY_OF_MONTH, 1);


        if(this.tourService.findTourById(id)!=null) {

            TourStart tourStart = new TourStart();

            tourStart.setTour_id(id);
            tourStart.setNgay_khoi_hanh(calendar.getTime());

            return new ResponseDTO("Thêm thành công",this.tourStartRepository.save(tourStart));
        }

        return new ResponseDTO("Tour không tồn tại khi thêm",null);
    }

}
