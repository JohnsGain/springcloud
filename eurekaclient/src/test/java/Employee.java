import javax.persistence.MappedSuperclass;

/**
 * @author Zhang juwa
 * @description
 * @email 1047994907@qq.com
 * @date 2018/11/28
 * @since jdk1.8
 **/
@MappedSuperclass
public class Employee {

    protected Long id;

    protected Integer version;

    protected String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
