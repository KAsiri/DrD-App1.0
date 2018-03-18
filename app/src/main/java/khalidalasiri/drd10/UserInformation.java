package khalidalasiri.drd10;

/**
 * Created by kasir on 3/4/2018.
 */

class UserInformation {

    private int _ID;
    private String _FirstName ;
    private String _EMail ;
    private String _Username ;
    private String _Password ;
    private String _UserType ;

    public UserInformation(int _ID, String _FirstName, String _EMail, String _Username, String _Password, String _UserType) {
        this._ID = _ID;
        this._FirstName = _FirstName;
        this._EMail = _EMail;
        this._Username = _Username;
        this._Password = _Password;
        this._UserType = _UserType;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String get_FirstName() {
        return _FirstName;
    }

    public void set_FirstName(String _FirstName) {
        this._FirstName = _FirstName;
    }

    public String get_EMail() {
        return _EMail;
    }

    public void set_EMail(String _EMail) {
        this._EMail = _EMail;
    }

    public String get_Username() {
        return _Username;
    }

    public void set_Username(String _Username) {
        this._Username = _Username;
    }

    public String get_Password() {
        return _Password;
    }

    public void set_Password(String _Password) {
        this._Password = _Password;
    }

    public String get_UserType() {
        return _UserType;
    }

    public void set_UserType(String _UserType) {
        this._UserType = _UserType;
    }
}
