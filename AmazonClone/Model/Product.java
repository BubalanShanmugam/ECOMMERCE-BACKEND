package com.example.AmazonClone.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id//primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private String category;

//  this would be handled by the UI
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
     private Date releaseDate;
    private boolean productAvailable;
    private int stockQuantity;

    private String imageName;
    private String imageType;

    //one way is the storing the data in the DB as a Link.
    //another way is storing the actual image in the DB.for this the mage's data should be very large.so we are using the @Lob
    @Lob//Large Object
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private  byte[] imageData;

}
