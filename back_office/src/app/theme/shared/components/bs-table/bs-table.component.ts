import {
  AfterViewChecked,
  AfterViewInit,
  Component, EventEmitter, Input,
  OnChanges,
  OnInit, Output,
  SimpleChanges
} from "@angular/core";
import * as _ from 'lodash';
import {BsTableColonneModel} from "./model/bs-table-colonne.model";
import {ButtonOverlayModel} from "./model/button-overlay.model";

@Component({
  selector: 'app-bs-table',
  templateUrl: './bs-table.component.html',
  styleUrls: ['./bs-table.component.scss']
})
export class BsTableComponent implements OnInit, OnChanges, AfterViewInit, AfterViewChecked {

  /* card param */
  @Input() cardTitle: string;
  @Input() cardClass: string;
  @Input() blockClass: string;
  @Input() headerClass: string;
  @Input() options: boolean;
  @Input() hidHeader: boolean;
  @Input() customHeader: boolean;
  @Input() cardCaption: string;
  @Input() captionClass: string;
  @Input() isCardFooter: boolean;
  @Input() footerClass: string;

  /* table param */
  @Input() public idTable: string = 'idTable';
  @Input() public tableClass: string;
  @Input() public colonnesName: Array<BsTableColonneModel>;
  @Input() public bsTableData: any[] = [];
  public bsTableDataLocal: any[];
  @Output() lineClicked = new EventEmitter();

  /* pagination param */
  @Input() public lazyLoading: boolean;
  @Output() loadingNewPage = new EventEmitter();
  @Input() public pageSize = 10;
  @Input() public totalSize = 0;
  @Input() public currentPageNumber = 0;
  @Input() public maxSize = 3;
  private paginationActive: boolean;
  public rotate: boolean = true;

  /* checkbox param */
  @Input() public equalsAttributes = ['id'];
  @Output() checkAllAction = new EventEmitter();
  public checkAll = false;
  public isChecked = false;
  public checkedLines = [];
  public uncheckedLines = [];
  public checkAllActive = false;
  public countSelectedObject = 0;

  /* table param overlay */
  @Input() public overlayActiveButton: boolean;
  @Input() public overlayListButton: Array<ButtonOverlayModel> = new Array<ButtonOverlayModel>();
  @Output() overlayButtonAction = new EventEmitter();
  @Input() myFields = [];
  public showButtonOverlay: boolean;

  // other component
  @Input() sort: any = {};
  @Output() sortAction = new EventEmitter();
  ngOnInit(): void {
    this.initMaxSizePagination();
    this.initOverlayButton();
    this.initDefaultValueCard();
    this.initDefaultValueTable();
    this.initTableFields();
  }
  public ngAfterViewChecked(): void {
  }

  public ngAfterViewInit(): void {
  }

  public ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes['bsTableData'] && changes['bsTableData'].previousValue !== changes['bsTableData'].currentValue) {
      if (changes['bsTableData'].previousValue && changes['bsTableData'].currentValue && changes['bsTableData'].previousValue.length ===  changes['bsTableData'].currentValue.length) {
        let missing = changes['bsTableData'].currentValue.filter(item => {
          let tmpElm = changes['bsTableData'].previousValue.filter(itm2 => this.equals(item, itm2));
          if (tmpElm && tmpElm.length > 0) {
            return false;
          } else {
            return true;
          }
        });
        if (missing && missing.length > 0) {
          this.initDefaultValueTable();
        }
      } else {
        this.initDefaultValueTable();
      }

    }
  }
  private initTableFields(): any {
    if (!this.myFields || this.myFields.length === 0) {
      this.myFields = this.colonnesName.map(col => {
        return col.colonneNameId;
      });
    }
  }

  private initDefaultValueTable(): void {
    if (!this.tableClass) {
      this.tableClass = 'table table-striped row-border table-hover';
    }
    if (this.bsTableData && this.bsTableData.length > this.pageSize) {
      this.paginationActive = true;
    }
    if (this.paginationActive && this.bsTableData.length > this.pageSize) {
      this.loadPage(this.currentPageNumber);
    } else {
      this.initCurentDataFromOriginalData(0);
    }
    if (this.currentPageNumber <= 0) {
      this.currentPageNumber = 1;
    }
    if (!this.lazyLoading) {
      this.totalSize = this.bsTableData ? this.bsTableData.length : 0;
    }

  }

  public loadPage(event: number): any {
    if (this.lazyLoading) {
      this.loadingNewPage.emit(event);
    } else {
      let startPage : number = (Number(event) * this.pageSize) - this.pageSize;
      if (startPage < 0) {
        startPage = 0
      }
      this.initCurentDataFromOriginalData(startPage);
    }
  }
  private initCurentDataFromOriginalData(startElement: number): void {
    this.bsTableDataLocal = this.bsTableDataSorted.slice(startElement, startElement + this.pageSize);
  }

  get bsTableDataSorted(): any[] {
    if (this.sort && this.sort.column) {
      return this.sortValues(this.bsTableData, this.sort.descending ? 'desc' : 'asc', this.sort.column);
    } else {
      return this.bsTableData ? this.bsTableData : [];
    }
  }

  private sortValues(value: any[], order = '', column: string = ''): any[] {
    if (!value || order === '' || !order) {
      return value;
    }
    if (value.length <= 1) {
      return value;
    }
    if (!column || column === '') {
      if (order === 'asc') {
        return value.sort()
      } else {
        return value.sort().reverse();
      }
    }
    return _.orderBy(value, value.length != 0 &&
    !value.some(v => v[column] == null) &&
    (typeof value[0][column] === 'string' || value[0][column] instanceof String) ?
        [object => object[column].toLowerCase()] : column, [order]);
  }

  private initDefaultValueCard(): void {
    if (!this.cardClass) {
      this.cardClass = 'user-profile-list';
    }
  }
  private initOverlayButton(): void {
    if (this.overlayActiveButton) {
      this.showButtonOverlay = true;
    }
  }

  private initMaxSizePagination(): void {
    if (window.innerWidth < 991) {
      this.maxSize = 1;
    }
  }
  public checkboxAllEmitter(event): void {
    if (event.target.checked) {
      this.checkAllLines();
    } else {
      this.unCheckAllLines();
    }
    this.checkAllAction.emit(this.checkAllActive);
  }
  private checkAllLines(): void {
    this.checkAll = true;
    this.checkAllActive = true;
    this.isChecked = true;
    this.checkedLines = _.clone(this.bsTableData);
    this.uncheckedLines = [];
  }

  private unCheckAllLines(): void {
    this.checkAll = false;
    this.checkAllActive = false;
    this.checkedLines = [];
    this.uncheckedLines = _.clone(this.bsTableData);
    this.isChecked = false;
  }

  public selectedClass(columnName): any {
    return columnName === this.sort.column
        ? 'fa fa-sort-' + (this.sort.descending ? 'down' : 'up')
        : 'fa fa-sort';
  }
  public changeSorting(columnName): void {
    const sort = this.sort;
    if (sort.column === columnName) {
      if (!sort.descending) {
        sort.descending = !sort.descending;
      } else {
        sort.column = null;
      }
    } else {
      sort.column = columnName;
      sort.descending = false;
    }
    if (this.lazyLoading) {
      this.sortAction.emit(this.sort);
    }
    this.loadPage(this.currentPageNumber);
  }

  public lineClickdAction(objectClicked: any): void {
    this.lineClicked.emit(objectClicked);
  }

  public getLabel(lineData): string {
    return this.equalsAttributes.map(attribute => lineData[attribute].toString()).join('_') + '_' + this.idTable;
  }

  public checkboxEmitter(event, lineData): void {
    if (event.target.checked) {
      this.checkedLines.push(lineData);
      this.isChecked = true;
      if (this.checkedLines.length === this.totalSize) {
        this.checkAllActive = true;
      }
    } else {
      this.checkedLines = this.checkedLines.filter(element => !this.equals(element, lineData));
      this.isChecked = this.checkedLines.length > 0;
      this.checkAllActive = false;
      this.uncheckedLines.push(lineData);
    }
  }
  public existInUncheckedLines(lineData: any) {
    return this.uncheckedLines.some(line => this.equals(lineData, line));
  }
  public existInCheckedLines(lineData): boolean {
    return this.checkedLines.some(line => this.equals(lineData, line));
  }
  equals(obj1, obj2): boolean {
    let isEqual = false;
    for (const attribute of this.equalsAttributes) {
      isEqual = obj1[attribute].toString() === obj2[attribute].toString();
      if (!isEqual) {
        return isEqual;
      }
    }
    return isEqual;
  }
  public isDate(lineValue: any): boolean {
    if (lineValue instanceof Date) {
      return true;
    } else {
      return false;
    }
  }
  public overlayButtonActionDo(buttonOverlay: ButtonOverlayModel, lineData: any): void {
    const objToEmmit = {
      buttonOverlayId: buttonOverlay.id,
      objectClicked: lineData
    };
    this.overlayButtonAction.emit(objToEmmit);
  }

  public isActions(lineData, buttonOverlay, field): boolean {
    if (lineData.actions != null && lineData.actions !== '') {
      const result = lineData.actions.find(e => e.action === buttonOverlay.tooltipButton);
      if (field === 'actions' && result) {
        return result.show;
      }
    }
    return true;
  }

  public resolve(path, obj): any {
    return path.split('.').reduce((prev, curr) => {
      if (prev instanceof Array) {
        const list = [];
        prev.forEach((element) => {
          list.push(element[curr]);
        });
        return list;
      }
      return prev ? prev[curr] : null;
    }, obj || self);
  }
}
