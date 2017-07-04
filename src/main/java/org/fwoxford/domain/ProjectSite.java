package org.fwoxford.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProjectSite.
 */
@Entity
@Table(name = "project_site")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectSite extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100, nullable = false)
    private String projectSiteCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "project_site_name", length = 255, nullable = false)
    private String projectSiteName;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @Size(max = 1024)
    @Column(name = "area", length = 1024)
    private String area;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "detailed_location", length = 1024)
    private String detailedLocation;

    @Size(max = 1024)
    @Column(name = "department", length = 1024)
    private String department;

    @Size(max = 1024)
    @Column(name = "detailed_address", length = 1024)
    private String detailedAddress;

    @Size(max = 20)
    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Size(max = 100)
    @Column(name = "username_1", length = 100)
    private String username1;

    @Size(max = 100)
    @Column(name = "phone_number_1", length = 100)
    private String phoneNumber1;

    @Size(max = 100)
    @Column(name = "username_2", length = 100)
    private String username2;

    @Size(max = 100)
    @Column(name = "phone_number_2", length = 100)
    private String phoneNumber2;

    @OneToMany(mappedBy = "projectSite")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProjectRelate> projectRelates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public ProjectSite projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public ProjectSite projectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
        return this;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getMemo() {
        return memo;
    }

    public ProjectSite memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getArea() {
        return area;
    }
    public ProjectSite area(String area) {
        this.area = area;
        return this;
    }
    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public ProjectSite status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetailedLocation() {
        return detailedLocation;
    }

    public ProjectSite detailedLocation(String detailedLocation) {
        this.detailedLocation = detailedLocation;
        return this;
    }

    public void setDetailedLocation(String detailedLocation) {
        this.detailedLocation = detailedLocation;
    }

    public String getDepartment() {
        return department;
    }

    public ProjectSite department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public ProjectSite detailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
        return this;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public ProjectSite zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUsername1() {
        return username1;
    }

    public ProjectSite username1(String username1) {
        this.username1 = username1;
        return this;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public ProjectSite phoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
        return this;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getUsername2() {
        return username2;
    }

    public ProjectSite username2(String username2) {
        this.username2 = username2;
        return this;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public ProjectSite phoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
        return this;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public Set<ProjectRelate> getProjectRelates() {
        return projectRelates;
    }

    public ProjectSite projectRelates(Set<ProjectRelate> projectRelates) {
        this.projectRelates = projectRelates;
        return this;
    }

    public ProjectSite addProjectRelate(ProjectRelate projectRelate) {
        this.projectRelates.add(projectRelate);
        projectRelate.setProjectSite(this);
        return this;
    }

    public ProjectSite removeProjectRelate(ProjectRelate projectRelate) {
        this.projectRelates.remove(projectRelate);
        projectRelate.setProjectSite(null);
        return this;
    }

    public void setProjectRelates(Set<ProjectRelate> projectRelates) {
        this.projectRelates = projectRelates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectSite projectSite = (ProjectSite) o;
        if (projectSite.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectSite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectSite{" +
            "id=" + id +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", projectSiteName='" + projectSiteName + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", detailedLocation='" + detailedLocation + "'" +
            ", department='" + department + "'" +
            ", detailedAddress='" + detailedAddress + "'" +
            ", zipCode='" + zipCode + "'" +
            ", username1='" + username1 + "'" +
            ", phoneNumber1='" + phoneNumber1 + "'" +
            ", username2='" + username2 + "'" +
            ", phoneNumber2='" + phoneNumber2 + "'" +
            '}';
    }
}
