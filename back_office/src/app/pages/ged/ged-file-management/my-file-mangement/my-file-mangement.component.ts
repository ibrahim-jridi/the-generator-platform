import { Component, OnInit } from '@angular/core';
//import { AuthenticationService } from 'src/app/services/authentication/authentication-service.service';

@Component({
  selector: 'app-my-file-mangement',
  templateUrl: './my-file-mangement.component.html',
  styleUrls: ['./my-file-mangement.component.scss']
})
export class MyFileMangementComponent implements OnInit {

  constructor(
    //private authenticationService:AuthenticationService
  ) { }
  checkMyFile:string="";
  ngOnInit() {
    /*
    if (this.checkPermissions("MY_FILE")){
        this.checkMyFile="MyFile"
    }
     */
  }

  /*
  checkPermissions(code: string): boolean {
    return this.authenticationService.hasCategoriePermission(code);
   }
   */
}
