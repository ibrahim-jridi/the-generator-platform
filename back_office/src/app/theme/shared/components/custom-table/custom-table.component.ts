import { AfterViewChecked, AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import * as _ from 'lodash';
import { CustomTableColonneModel } from './model/custom-table-colonne.model';
import { ButtonOverlayModel } from './model/button-overlay.model';
import { ButtonActionModel } from './model/button-action.model';
import { ExportService } from '../../../../shared/services/export.service';
import { TranslatePipe } from '@ngx-translate/core';
import { TokenUtilsService } from '../../../../shared/services/token-utils.service';
import { FormGroup } from '@angular/forms';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';

interface TableStat {
  name: string;
  count: number;
}

@Component({
  selector: 'app-custom-table',
  templateUrl: './custom-table.component.html',
  styleUrls: ['./custom-table.component.scss']
})
export class CustomTableComponent implements OnInit, OnChanges, AfterViewInit, AfterViewChecked {

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
  @Input() noBorder: boolean;
  @Input() public banner: boolean = false;
  /* table param */
  @Input() public idTable: string = 'idTable';
  @Input() public tableClass: string;
  @Input() public colonnesName: Array<CustomTableColonneModel>;
  @Input() public columnsClass: string;
  @Input() public tollTableData: any[] = [];
  public tollTableDataLocal: any[];
  @Output() lineClicked = new EventEmitter();
  @Input() public Tablename: string;

  /* pagination param */
  @Input() public lazyLoading: boolean;
  @Output() loadingNewPage = new EventEmitter();
  @Input() public pageSize = 10;
  @Output() pageSizeChange = new EventEmitter<number>();
  @Input() public totalSize = 0;
  @Input() public currentPageNumber = 0;
  @Input() public maxSize = 3;
  public pageSizes = [5, 10, 25, 50];
  private paginationActive: boolean;
  public rotate: boolean = true;
  public showOptions: boolean = false;

  /* checkbox param */
  @Input() public equalsAttributes = ['id'];
  @Output() checkAllAction = new EventEmitter();
  @Output() itemChecked = new EventEmitter();
  @Output() itemUnChecked = new EventEmitter();
  public checkAll = false;
  public isChecked = false;
  public checkedLines = [];
  public uncheckedLines = [];
  public checkAllActive = false;
  public countSelectedObject = 0;

  /* table param overlay */
  @Input() public overlayActiveButton: boolean;
  @Input() public actionActiveButton: boolean;
  @Input() public activeFilter: boolean;
  @Input() public activeFilterInput: boolean;
  @Input() public activeExport: boolean;
  @Input() public overlayListButton: Array<ButtonOverlayModel> = new Array<ButtonOverlayModel>();
  @Output() overlayButtonAction = new EventEmitter();
  @Output() buttonAction = new EventEmitter();
  @Input() public actionListButton: Array<ButtonActionModel> = new Array<ButtonActionModel>();
  @Input() myFields = [];
  public showButtonOverlay: boolean;
  public showButtonAction: boolean;

  // Toggle view & Card view
  @Input() public viewToggle: boolean;
  @Input() public cardViewType: "user-view" | "gate-view";
  public isCardView: boolean;
  public isSuperAdmin: boolean = true;

  @Input() buttonType: string;
  @Input() buttonText: string;
  @Input() buttonClass: string;
  @Input() buttonIcon: string;
  @Input() buttonDisabled: boolean;
  @Output() clicked: EventEmitter<any> = new EventEmitter<any>();

  // other component
  @Input() sort: any = {};
  @Output() sortAction = new EventEmitter();
  public searchText: string = '';

  //Banner
  public form: FormGroup;
  public startDate: Date | undefined;
  public endDate: Date | undefined;
  public selectedDateRange: string;
  public selectedColumn: string | null = null;
  public openFilters: Set<string> = new Set();

  // 2. Add a property to hold the statistics data
  @Input() public totalStats: TableStat[] = [];
  @Output() rangeDate = new EventEmitter<{ start: Date | null, end: Date | null }>();

  ngOnInit(): void {
    this.initMaxSizePagination();
    this.initOverlayButton();
    this.initActionButtons();
    this.initDefaultValueCard();
    this.initDefaultValueTable();
    this.initTableFields();
    this.colonnesName.forEach(column => {
      column.filterValue = '';
    });
    this.isSuperAdmin = true;
  }

  constructor(private exportService: ExportService, private translatePipe: TranslatePipe, private tokenUtilsService: TokenUtilsService) {
  }

  public ngAfterViewChecked(): void {
  }

  public ngAfterViewInit(): void {
  }

  public ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes['tollTableData'] && changes['tollTableData']?.previousValue !== changes['tollTableData']?.currentValue) {
      this.initDefaultValueTable();
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
    if (this.tollTableData && this.tollTableData.length > this.pageSize) {
      this.paginationActive = true;
    }
    if (this.paginationActive && this.tollTableData.length > this.pageSize) {
      this.loadPage(this.currentPageNumber);
    } else {
      this.initCurentDataFromOriginalData(0);
    }
    if (this.currentPageNumber <= 0) {
      this.currentPageNumber = 1;
    }
    if (!this.lazyLoading) {
      this.totalSize = this.tollTableData ? this.tollTableData.length : 0;
    }

  }

  public loadPage(event: number, columnName?: string, filterValue?: string): void {
    this.currentPageNumber = event;
    if (this.lazyLoading) {
      const filterValues = this.colonnesName.filter(col => col.filterValue.length > 0).map(col => {
        return {
          columnName: col.colonneNameId,
          filterValue: col.filterValue,
        };
      });
      this.loadingNewPage.emit({pageNumber: this.currentPageNumber, filterValues});
      this.initCurentDataFromOriginalData(0);
    } else {
      let startPage: number = (this.currentPageNumber - 1) * this.pageSize; // Calculate start index
      if (startPage < 0) {
        startPage = 0;
      }
      this.initCurentDataFromOriginalData(startPage);
    }
  }

  private initCurentDataFromOriginalData(startElement: number): void {
    this.tollTableDataLocal = this.tollTableDataSorted.slice(startElement, startElement + this.pageSize);
  }

  get tollTableDataSorted(): any[] {
    if (this.sort && this.sort.column) {
      return this.sortValues(this.tollTableData, this.sort.descending ? 'desc' : 'asc', this.sort.column);
    } else {
      return this.tollTableData ? this.tollTableData : [];
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

  private initActionButtons(): void {
    if (this.actionActiveButton) {
      this.showButtonAction = true;
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
    this.checkedLines = _.clone(this.tollTableData);
    this.uncheckedLines = [];
  }

  private unCheckAllLines(): void {
    this.checkAll = false;
    this.checkAllActive = false;
    this.checkedLines = [];
    this.uncheckedLines = _.clone(this.tollTableData);
    this.isChecked = false;
  }

  public selectedClass(columnName): any {
    let sortClass = 'fa fa-sort';
    if (columnName === this.sort.column) {
      sortClass += this.sort.descending ? '-down' : '-up';
    }
    return sortClass;
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
      this.itemChecked.emit(lineData);
      this.isChecked = true;
      if (this.checkedLines.length === this.totalSize) {
        this.checkAllActive = true;
      }
    } else {
      this.checkedLines = this.checkedLines.filter(element => !this.equals(element, lineData));
      this.isChecked = this.checkedLines.length > 0;
      this.checkAllActive = false;
      this.uncheckedLines.push(lineData);
      this.itemUnChecked.emit(lineData);

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
      return !isNaN(lineValue.getTime());
    } else if (typeof lineValue === 'string') {
      const regex = /^\d{4}-\d{2}-\d{2}$/;

      // Check if the string matches the date format
      if (regex.test(lineValue)) {
        const date = new Date(lineValue);
        return !isNaN(date.getTime());
      }
    }
    return false;
  }

  public overlayButtonActionDo(buttonOverlay: ButtonOverlayModel, lineData: any): void {
    const objToEmmit = {
      buttonOverlayId: buttonOverlay.id,
      objectClicked: lineData
    };
    this.overlayButtonAction.emit(objToEmmit);
  }

  public buttonActionDo(buttonAction: ButtonActionModel, lineData: any): void {
    const objToEmmit = {
      buttonActionId: buttonAction.id,
      objectClicked: lineData
    };
    this.buttonAction.emit(objToEmmit);
  }

  public isActions(lineData, buttonOverlay, field): boolean {
    if (buttonOverlay.hiddenRule) {
      return !buttonOverlay.hiddenRule(lineData);
    }
    if (lineData.actions != null && lineData.actions !== '') {
      const result = lineData.actions.find(e => e.action === buttonOverlay.tooltipButton);
      if (result) {
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

  getFormattedValue(column: CustomTableColonneModel, value: any): string {
    if (column.formatter) {
      return column.formatter(value);
    }
    return value;
  }

  public filterColumn(column: CustomTableColonneModel): void {
    if (column.filterValue.length >= 1) {
      this.filterData(column);
    } else {
      this.clearFilter(column);
    }
  }

  public toggleSearch(): void {
    this.activeFilter = !this.activeFilter;
    if (!this.activeFilter) {
      this.pageSizeChange.emit(5);
    }
  }

  private filterData(column: CustomTableColonneModel): void {
    if (this.lazyLoading) {
      this.loadPage(this.currentPageNumber, column.colonneNameId, column.filterValue);
      this.filterDataLocally(column);
    } else {
      this.filterDataLocally(column);
    }
  }

  private filterDataLocally(column: CustomTableColonneModel): void {
    const filteredData = this.tollTableData.filter(item => {
      for (const column of this.colonnesName) {
        const itemValue = item[column.colonneNameId];
        const filterValue = column.filterValue;

        // Check if the column name suggests it's a date field
        if (column.colonneName.toLowerCase().includes('date')) {
          if (itemValue && filterValue) {
            // Parse the date from DD/MM/YYYY format
            const itemDateParts = itemValue.split('/');
            const filterDateParts = filterValue.split('-');

            // Create date objects from parts
            const itemDate = new Date(parseInt(itemDateParts[2]), parseInt(itemDateParts[1]) - 1, parseInt(itemDateParts[0]));
            const filterDate = new Date(parseInt(filterDateParts[0]), parseInt(filterDateParts[1]) - 1, parseInt(filterDateParts[2]));
            // Set hours to zero for both dates
            itemDate.setHours(0, 0, 0, 0);
            filterDate.setHours(0, 0, 0, 0);

            // Compare the dates
            if (itemDate.getTime() !== filterDate.getTime()) {
              return false;
            }
          }
        } else {
          if (filterValue && filterValue != '') {
            if (itemValue !== undefined && itemValue !== null) {
              const itemValueStr = itemValue.toString().toLowerCase();
              const filterValueStr = filterValue.toString().toLowerCase();
              if (!itemValueStr.includes(filterValueStr)) {
                return false;
              }
            } else {
              return false;
            }
          }
        }
      }
      return true;
    });
    if (this.tollTableData.length == filteredData.length) {
      this.initDefaultValueTable();
    } else {
      this.tollTableDataLocal = filteredData;
    }

  }

  private clearFilter(column: CustomTableColonneModel): void {
    if (this.lazyLoading) {
      this.loadPage(this.currentPageNumber, column.colonneNameId, column.filterValue);
      this.filterDataLocally(column);
    } else {
      this.loadPage(this.currentPageNumber);
      this.filterDataLocally(column);
    }
  }

  public exportTable(exportType: string): void {
    if (this.checkedLines.length === 0) {
      this.checkAllLines();
    }
    this.exportService.exportTable(exportType, this.checkedLines, this.colonnesName, this.Tablename);
  }

// Filter table data based on the search text
  public filterTableData(): void {
    if (this.searchText.trim() === '' || this.searchText === '') {
      // If search text is empty, reset to the original table data
      this.resetFilter();
    } else {
      // Filter the table data based on the search text for each column
      this.tollTableDataLocal = this.tollTableData.filter(item => {
        return this.colonnesName.some(column => {
          const columnValue = item[column.colonneNameId];
          return columnValue && columnValue.toString().toLowerCase().includes(this.searchText.toLowerCase());
        });
      });
    }
  }

  // Reset the filter to the original table data
  private resetFilter(): void {
    this.tollTableDataLocal = this.tollTableData;
    this.totalSize = this.tollTableData.length;
    this.loadPage(this.currentPageNumber);
  }

  public toggleOptions(): void {
    this.showOptions = !this.showOptions;
  }

  public handleKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.toggleOptions();
    }
  }

  setViewMode(cardView: boolean): void {
    this.isCardView = cardView;
  }

  public selectPageSize(size: any): void {
    this.currentPageNumber = 0;
    this.pageSize = size;
    this.showOptions = false;
    this.pageSizeChange.emit(size);
    this.toggleOptions();
    if (!this.lazyLoading) {
      this.onPageSizeChange()
    }
  }

  public clickOutsideOptions(): void {
    this.showOptions = false;
  }

  public onPageSizeChange(): void {
    this.currentPageNumber = 0;
    this.loadPage(this.currentPageNumber);
  }


  // Fonction pour gérer le changement de date
  public dateRange(dateType: 'start' | 'end', event: MatDatepickerInputEvent<Date>): void {
    const date = event.value;
    if (dateType === 'start') {
      this.startDate = date;
    } else if (dateType === 'end') {
      this.endDate = date;
    }
    this.rangeDate.emit({start: this.startDate, end: this.endDate});
  }

  protected showFilterInput(columnName: string): void {
    this.openFilters.add(columnName);
    if (!this.activeFilterInput && !this.lazyLoading) {
      this.activeFilterInput = !this.activeFilterInput;
    }
  }

  protected hideFilterInput(column: CustomTableColonneModel): void {
    column.filterValue = '';
    this.openFilters.delete(column.colonneName);
    if (this.openFilters.size === 0) {
      this.activeFilterInput = false;
      if (!this.lazyLoading) {
        this.currentPageNumber = 0;
      }
    }
    this.clearFilter(column);
  }
}
