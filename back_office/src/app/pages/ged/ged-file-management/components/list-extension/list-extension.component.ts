import {Component, OnInit, ViewChild} from '@angular/core';
import {DataTableComponent} from "../data-table/data-table.component";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-list-extension',
  templateUrl: './list-extension.component.html',
  styleUrls: ['./list-extension.component.scss']
})
export class ListExtensionComponent implements OnInit {
 @ViewChild('dataTableComponent' , {static : false})
 dataTableComponent : DataTableComponent;
  pageSize: number = 10;
  pageIndex: number = 0;
  pages: number = 0;
  totalElements: number = 0;
  sizeConfigs: any [] = [];

  constructor() {}

  // TODO: Refactor findSizeConfigs
  findSizeConfigs() {}

  ngOnInit() {}

  refresh($event: any) {
    this.findSizeConfigs();
  }

  changePage($event: PageEvent) {
    this.pageIndex = $event['pageIndex'];
    this.findSizeConfigs()
  }

  search($event: string) {
    if( $event != '' ||  $event ) {
     // this.searchSizeConfigRequest.extension = $event;
      this.pageIndex = 0;
      this.findSizeConfigs()
    }else {
      //this.searchSizeConfigRequest.extension = null;
      this.findSizeConfigs();
    }
  }
}
