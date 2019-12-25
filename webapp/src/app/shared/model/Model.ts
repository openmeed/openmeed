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

export class CriteriaDetail {
  message: string;
  key: string;
  value: string;

  constructor() {
  }
}

export class Reward {
  issueId: string = null;
  value: string = null;
  authorizer: string = null;
  type: string = "pts";
  claimConstraints = {};
  timeConstraints = {};

  constructor() {
  }
}

export class Issue {
  url: string;
  id: number;
  html_url: string;
  state: string;
  number: number;
  title: string;
  created_at: Date;
  updated_at: Date;
  locked: boolean;
  assignees: object;
  labels: [];
  author_association: string;

  constructor() {
  }
}

