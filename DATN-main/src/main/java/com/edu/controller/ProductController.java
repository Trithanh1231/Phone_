package com.edu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.Utils.SessionUtil;
import com.edu.model.Favorite;
import com.edu.model.InventoryReport;
import com.edu.model.Product;
import com.edu.model.Review;
import com.edu.model.User;
import com.edu.model.Voucher;
import com.edu.reponsitory.FavoriteRepository;
import com.edu.reponsitory.ProductReponsitory;
import com.edu.reponsitory.ReviewReponsitory;
import com.edu.reponsitory.VouCherReponsitory;
import com.edu.service.BrandsService;
import com.edu.service.MultimageService;
import com.edu.service.ProductService;
import com.edu.service.UserService;

@Controller
public class ProductController {
    @Autowired
    BrandsService brandsService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    MultimageService multimageService;

    @Autowired
    ProductReponsitory productReponsitory;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    VouCherReponsitory vouCherReponsitory;

    @Autowired
    ReviewReponsitory reviewReponsitory;
    private final int PAGE_SIZE = 12;

    @RequestMapping("/shop")
    public String h1(Model model) {
        model.addAttribute("list", brandsService.findAll());
        Pageable pageable = PageRequest.of(0, 8);
        Page<Product> page = productReponsitory.findAll(pageable);
        Page<Product> page2 = productReponsitory.findByCreateDate(pageable);
        Page<Product> page3 = productReponsitory.finAllDiscount(pageable);
        model.addAttribute("pageProducts", page.getContent());
        model.addAttribute("op", page2.getContent());
        model.addAttribute("pa", page3.getContent());

        return "product/home";
    }

    @RequestMapping("product/list/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        model.addAttribute("list", productService.finID(id));
        model.addAttribute("list2", multimageService.findimage(id));
        model.addAttribute("review", reviewReponsitory.View(id));
//        model.addAttribute("count", reviewReponsitory.load(id));
//        model.addAttribute("favarite", favoriteRepository.findproduct(id));
        sessionUtil.setAttribute("fa", favoriteRepository.findAllwhere(request.getRemoteUser()));
        Optional<Favorite> fa = favoriteRepository.find(request.getRemoteUser(), id);
        if (fa.isPresent()) {
            model.addAttribute("ah", 1);
            model.addAttribute("favorite", favoriteRepository.findproduct(request.getRemoteUser(), id));
          
        } else {
            model.addAttribute("ah", 0);
            model.addAttribute("favorite", new Favorite());
            
        }
        return "product/detail";

    }

    @RequestMapping("shop/home")
    public String listall(Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Optional<Integer> pageIndex) {
        try {

            Pageable pageable = PageRequest.of(pageIndex.orElse(0), 12);
            Page<Product> page = productReponsitory.findAll(pageable);

            model.addAttribute("pageNumber", page.getNumber());
            model.addAttribute("pageTotalPages", page.getTotalPages());
            model.addAttribute("list", page.getContent());
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
            if (request.getRemoteUser() == null) {
                System.out.println("asa");
            } else {
                User u = userService.findID(request.getRemoteUser());
                System.out.println("ten: " + u.getFullname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "product/list";

    }

    @GetMapping("/product/findbyprice")
    public String find(Model model, @RequestParam(value = "min", required = false) Optional<Double> min,
            @RequestParam(value = "max", required = false) Optional<Double> max,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {
        List<Product> products = new ArrayList<>();
        double minPrice = min.orElse(Double.MIN_VALUE);
        double maxPrice = max.orElse(Double.MAX_VALUE);

        try {
            Double m1 = min.orElse(sessionUtil.getAttribute("min"));
            sessionUtil.setAttribute("min", m1);

            Double m2 = max.orElse(sessionUtil.getAttribute("max"));
            sessionUtil.setAttribute("max", m2);

            Page<Product> pageProducts = productService.findByPrice(PAGE_SIZE, pageNumber, m1, m2);
            products = pageProducts.getContent();
            model.addAttribute("pageTotalPages", pageProducts.getTotalPages());
            model.addAttribute("pageNumber", pageNumber);
            model.addAttribute("min", m1);
            model.addAttribute("max", m2);
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
            products = productService.findAll();
        }
        model.addAttribute("list", products);
        return "product/list";
    }

    @GetMapping("/product/search")
    public String searchProduct(Model model, @RequestParam(value = "keyword", required = false) Optional<String> kw,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {
        List<Product> products = new ArrayList<>();
        try {
            String keyword = kw.orElse(sessionUtil.getAttribute("keyword"));
            sessionUtil.setAttribute("keyword", keyword);
            Page<Product> pageProducts = productService.searchByKeyword(PAGE_SIZE, pageNumber, keyword);
            products = pageProducts.getContent();
            model.addAttribute("pageTotalPages", pageProducts.getTotalPages());
            model.addAttribute("pageNumber", pageNumber);
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            e.printStackTrace();
            products = productService.findAll();
        }
        model.addAttribute("list", products);
        return "product/list";
    }

    @GetMapping("/product/finbybrand/{name}")
    public String searchProductbybrand(Model model, @PathVariable(value = "name", required = false) Optional<String> n,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {
        List<Product> products = new ArrayList<>();
        try {
            String name = n.orElse(sessionUtil.getAttribute("name"));
            sessionUtil.setAttribute("name", name);
            Page<Product> pageProducts = productService.findByBrand(PAGE_SIZE, pageNumber, name);
            products = pageProducts.getContent();
            model.addAttribute("pageTotalPages", pageProducts.getTotalPages());
            model.addAttribute("pageNumber", pageNumber);
            model.addAttribute("name", name);
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
            products = productService.findAll();
        }
        model.addAttribute("list", products);
        return "product/list";
    }

    @GetMapping("/product/finbysize/{size}")
    public String searchProductbySize(Model model, @PathVariable(value = "size", required = false) Optional<Integer> s,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {
        List<Product> products = new ArrayList<>();
        try {
            Integer size = s.orElse(sessionUtil.getAttribute("size"));
            sessionUtil.setAttribute("size", size);
            Page<Product> pageProducts = productService.findBySize(PAGE_SIZE, pageNumber, size);
            products = pageProducts.getContent();
            model.addAttribute("pageTotalPages", pageProducts.getTotalPages());
            model.addAttribute("pageNumber", pageNumber);
            model.addAttribute("size", size);
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
            products = productService.findAll();
        }
        model.addAttribute("list", products);
        return "product/list";
    }

    // sort
    @RequestMapping("product/listall/atoz")
    public String listAtoZ(Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Optional<Integer> pageIndex) {
        try {

            Pageable pageable = PageRequest.of(pageIndex.orElse(0), 12);
            Page<Product> page = productReponsitory.sortAtoZ(pageable);

            model.addAttribute("pageNumber", page.getNumber());
            model.addAttribute("pageTotalPages", page.getTotalPages());
            model.addAttribute("list", page.getContent());
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "product/list";

    }

    @RequestMapping("product/listall/ztoa")
    public String listZtoA(Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Optional<Integer> pageIndex) {
        try {

            Pageable pageable = PageRequest.of(pageIndex.orElse(0), 12);
            Page<Product> page = productReponsitory.sortZtoA(pageable);

            model.addAttribute("pageNumber", page.getNumber());
            model.addAttribute("pageTotalPages", page.getTotalPages());
            model.addAttribute("list", page.getContent());
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "product/list";

    }

    @RequestMapping("product/listall/pricelowtohigh")
    public String listPricelow(Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Optional<Integer> pageIndex) {
        try {

            Pageable pageable = PageRequest.of(pageIndex.orElse(0), 12);
            Page<Product> page = productReponsitory.sortPriceLow(pageable);

            model.addAttribute("pageNumber", page.getNumber());
            model.addAttribute("pageTotalPages", page.getTotalPages());
            model.addAttribute("list", page.getContent());
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "product/list";

    }

    @RequestMapping("product/listall/pricehightolow")
    public String listPricehigh(Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Optional<Integer> pageIndex) {
        try {

            Pageable pageable = PageRequest.of(pageIndex.orElse(0), 12);
            Page<Product> page = productReponsitory.sortZtoA(pageable);

            model.addAttribute("pageNumber", page.getNumber());
            model.addAttribute("pageTotalPages", page.getTotalPages());
            model.addAttribute("list", page.getContent());
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "product/list";

    }

    @RequestMapping("product/listall/sortnew")
    public String listsortnew(Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Optional<Integer> pageIndex) {
        try {

            Pageable pageable = PageRequest.of(pageIndex.orElse(0), 12);
            Page<Product> page = productReponsitory.sortNew(pageable);

            model.addAttribute("pageNumber", page.getNumber());
            model.addAttribute("pageTotalPages", page.getTotalPages());
            model.addAttribute("list", page.getContent());
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "product/list";

    }

    @RequestMapping("product/listall/sortold")
    public String listold(Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Optional<Integer> pageIndex) {
        try {

            Pageable pageable = PageRequest.of(pageIndex.orElse(0), 12);
            Page<Product> page = productReponsitory.findByCreateDate(pageable);

            model.addAttribute("pageNumber", page.getNumber());
            model.addAttribute("pageTotalPages", page.getTotalPages());
            model.addAttribute("list", page.getContent());
            model.addAttribute("count", productService.count());
            model.addAttribute("load", productReponsitory.load());
            model.addAttribute("loadsize", productReponsitory.loadsize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "product/list";

    }

    @RequestMapping("/product/tip")
    public String tip() {
        return "hd/tip";
    }

    @RequestMapping("/product/note")
    public String tip2() {
        return "hd/tip2";
    }
    @RequestMapping("/wish/list")
    public String wish(Model model) {
        model.addAttribute("favorite", favoriteRepository.findAllwhere(request.getRemoteUser()));
        return "product/wish";
    }

    @RequestMapping("/review/add")
    public String add(Model model, @RequestParam("pid") Long id, @RequestParam("rating") Double rating,
            @RequestParam("comment") String comment) {
        try {
            User user = userService.findID(request.getRemoteUser());
            Product product = productService.finID(id);
            Date date = new Date();

            Review review = new Review();
            review.setComment(comment);
            review.setCreateDate(date);
            review.setProduct(product);
            review.setUser(user);
            review.setRating(rating);
            reviewReponsitory.save(review);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/product/list/" + id;

    }

    @RequestMapping("/product/voucher")
    public String voucher(Model model, @RequestParam("voucher_code") String voucher_code,@RequestParam("product_id") Long id) {
        try {
            Date date = new Date();
           if( StringUtils.hasText(voucher_code)) {
              if(vouCherReponsitory.find(id,date) == null) {
                  sessionUtil.setAttribute("giamgia", 0);
              }else {
                  Voucher voucher = vouCherReponsitory.find(id,date);
                  if(voucher.getVoucherCode().equalsIgnoreCase(voucher_code)) {
                      voucher.setMaxUser(voucher.getMaxUser()-1);
                      vouCherReponsitory.save(voucher);
                      
                      sessionUtil.setAttribute("giamgia", voucher.getDiscountAmount());
                  }else {
                      sessionUtil.setAttribute("giamgia", 0);
                      model.addAttribute("message", "Voucher không hợp lệ hoặc voucher đả hết hạn");
                  }
              }
           }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "cart/view";
    }

}
