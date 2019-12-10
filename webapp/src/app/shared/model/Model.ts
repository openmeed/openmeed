export class Model {
  name: string = null;
  username: string = null;
  password: string = null;
  career: string = null;
  points: number = null;

  constructor() {
  }
}

export class LoginRequest {
  usernameOrEmail: string = null;
  password: string = null;
  twoFA: boolean = null;

  constructor() {
  }
}

export class RegisterRequest {
  name: string = null;
  username: string = null;
  email: string = null;
  password: string = null;


  constructor() {
  }
}

export class Reward {
  issueId: string = null;
  value: string = null;
  reviewer: string = null;
  type: string = "pts";

  constructor() {
  }
}

