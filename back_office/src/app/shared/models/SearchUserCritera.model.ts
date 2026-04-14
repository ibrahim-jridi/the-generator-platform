export class SearchUserCritera {
  id?:any;
  username? :string;

  address? :string;
  profession? :string;
  firstName? :string;
  lastName? :string;



  constructor(
      id?: any,
      username?: string,
      firstName?: string,
      lastName?: string,
      address?: string,
      profession?: string,


      ) {
      this.id = id;

      this.username = username;
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
      this.profession = profession;


      }
}
