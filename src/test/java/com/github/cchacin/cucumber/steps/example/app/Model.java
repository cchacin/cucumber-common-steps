package com.github.cchacin.cucumber.steps.example.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class Model {
    String id;
    Date created;
    Date modified;
    String email;
    String fullname;
    String password;
}
