import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../../shared/services/global.service';
import { RequestType } from "../../shared/enums/requestType";
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-ectd',
  templateUrl: './ectd.component.html',
  styleUrls: ['./ectd.component.scss']
})
export class EctdComponent implements OnInit {

  constructor(
      private globalService: GlobalService
  ) {}

  ngOnInit(): void {
    const bsAccessToken = sessionStorage.getItem('token');

    if (!bsAccessToken) {
      console.error('No token found for bs_client');
      return;
    }

    window.location.href = "";
  }
}
