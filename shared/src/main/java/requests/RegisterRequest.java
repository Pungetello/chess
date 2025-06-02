package requests;

public record RegisterRequest (
    String username,
    String password,
    String email){

}

// other 5 requests here, in same file?