import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {FormBuilder, FormGroup} from "@angular/forms";


@Component({
  selector: 'app-data-table',
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.scss']
})
export class DataTableComponent implements OnInit, OnChanges {


  @Input() ELEMENT_DATA: any;
  @Output() event = new EventEmitter<any>()
  displayedColumns: string[] = ['id', 'extesnion', 'size', 'actions'];
  dataSource: any;
  sizeConfig: SizeConfig;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  pageSize: number = 4;
  pageIndex: number = 0;
  pages: number = 0;
  totalElements: number = 0;
  formInputFilter: FormGroup;
  showFilterInput: boolean = false;
  @Output() newDataListEvent = new EventEmitter<string>();

  ngOnChanges(changes: SimpleChanges): void {
    if (this.ELEMENT_DATA) {
      this.dataSource = new MatTableDataSource<SizeConfig>(this.ELEMENT_DATA);
      this.dataSource.paginator = this.paginator;
    }
  }

  constructor(public dialog: MatDialog,
              private formBuilder: FormBuilder,
  ) {


  }

  ngOnInit(): void {
    this.formInputFilter = this.createForm()
  }

  createForm() {
    return this.formBuilder.group({
      extension: '',
    });
  }

  clearSearch() {
    this.formInputFilter.reset();
    this.searchData(this.ELEMENT_DATA);
    this.showFilterInput = false
  }

  toggleFilterInput() {
    if (this.showFilterInput) {
      this.formInputFilter.reset()
    }
    this.showFilterInput = !this.showFilterInput;
    if (!this.showFilterInput) {
      this.clearSearch();
    }
  }

  searchData(newValue: string) {
    this.newDataListEvent.emit(newValue);
  }

  ngOnDestroy(): void {
    this.dialog.closeAll();
  }

  checkPermissions(code: string): boolean {
    return true
  }
}


export interface SizeConfig {
  id: string;
  extension: string;
  maxSize: number;
}


