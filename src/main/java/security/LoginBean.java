package security;

import service.LoginService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static javax.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@FacesConfig
@Named
@RequestScoped
public class LoginBean {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @Inject
    private LoginService ls;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private FacesContext facesContext;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    public String login() {
    //        if(ls.findAdmin(username, password)) {
    //            return "/sc/admin/home.xhtml?faces-redirect=true";
    //        }
    //        else if(ls.findUser1(username, password)) {
    //            return "/sc/user1/home.xhtml?faces-redirect=true";
    //        }
    //        else if(ls.findUser2(username, password)) {
    //            return "/sc/user2/home.xhtml?faces-redirect=true";
    //        }
    //        else
    //            JsfUtil.addErrorMessage("InvalidLogin");
    //            return "/sc/login.xhtml";
    //    }
    public void login() {
        Credential credential = new UsernamePasswordCredential(username, new Password(password));
        AuthenticationStatus status = securityContext.authenticate(
                getHttpRequestFromFacesContext(),
                getHttpResponseFromFacesContext(),
                withParams().credential(credential));
//        System.out.println(username);
//        System.out.println(password);
        if (status.equals(SEND_CONTINUE)) {
            facesContext.responseComplete();
        } else if (status.equals(SEND_FAILURE)) {
            facesContext.addMessage(null,
                    new FacesMessage(SEVERITY_ERROR, "Authentication failed", null));
        }
    }

    private HttpServletRequest getHttpRequestFromFacesContext() {
        return (HttpServletRequest) facesContext
                .getExternalContext()
                .getRequest();
    }

    private HttpServletResponse getHttpResponseFromFacesContext() {
        return (HttpServletResponse) facesContext
                .getExternalContext()
                .getResponse();
    }
}
