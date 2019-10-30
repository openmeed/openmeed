export class User {
  name: string = null;
  username: string = null;
  password: string = null;
  career: string = null;

  constructor(){}
}

export class LoginRequest {
  usernameOrEmail: string = null;
  password: string = null;
  twoFA:boolean = null;

  constructor(){}
}

export class RegisterRequest {
  name: string = null;
  username: string = null;
  email: string = null;
  password: string = null;
  career: string = null;
  company: string = null;


  constructor(){}
}

export class TwoFactor {
  code: string = null;
  username: string = null;
  constructor(){}
}
