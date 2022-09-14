package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "certificate")
public class Certificate extends BaseObject {
    private static final long serialVersionUID = 1L;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "granted_by")
    private String grantedBy;

    @Column(name = "valid_date")
    private LocalDate validDate;

    @Column(name = "exp_date")
    private LocalDate expDate;

    @OneToMany(mappedBy = "certificate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CertificateEmployee> employeeCertificate = new ArrayList<>();


}
