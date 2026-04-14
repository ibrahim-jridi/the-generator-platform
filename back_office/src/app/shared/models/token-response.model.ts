export class TokenResponse {
  access_token: string;
  expires_in: string;
  refresh_expires_in: string;
  refresh_token: string;
  token_type: string;
  id_token: string;
  session_state: string;
  userId: string;
  emailVerified: boolean;
  isProfileCompleted: boolean;
  email: string;
  gender: string;
  firstName: string;
  lastName: string;
  username: string;
  nationality: string;
  country: string;
  cin: string;
  eBarid: string;
  passportAttachment: string;
  passportNumber: string;
  birthDate: string;
  phoneNumber: string;
  address: string;

  constructor() {
    this.access_token = '';
    this.expires_in = '';
    this.refresh_expires_in = '';
    this.refresh_token = '';
    this.token_type = '';
    this.id_token = '';
    this.session_state = '';
    this.userId = '';
    this.emailVerified = false;
    this.isProfileCompleted = false;
    this.email = '';
    this.gender = '';
    this.firstName = '';
    this.lastName = '';
    this.username = '';
    this.nationality = '';
    this.country = '';
    this.cin = '';
    this.eBarid = '';
    this.passportAttachment = '';
    this.passportNumber = '';
    this.birthDate = '';
    this.phoneNumber = '';
    this.address = '';
  }
}
