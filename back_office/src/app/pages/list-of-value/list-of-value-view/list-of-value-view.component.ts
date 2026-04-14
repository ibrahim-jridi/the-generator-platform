import { Component, Input, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { ListOfValueService } from 'src/app/shared/services/list-of-value.service';
import { Subscription } from 'rxjs';
import { dE } from '@fullcalendar/core/internal-common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-list-of-value-view',
  templateUrl: './list-of-value-view.component.html',
  styleUrls: ['./list-of-value-view.component.scss']
})
export class ListOfValueViewComponent implements OnInit, OnDestroy {
  formData: any[] = []; 
  multipleData: any
  ismultiple: boolean;
  private subscription: Subscription;

  @Input() items: any[] = [];
  @Input() displayFn: (item: any) => string;
  @Input() placeholder: string;
  @Output() selectionChange = new EventEmitter<any>();
  @Input() ngClass: string = '';
  public state = window.history.state;
  private valueId: string;
  showOptions = false;
  selectedItem: any;

  private onChange: any = () => {};
  private onTouched: any = () => {};
  private globalClickListener: () => void;

  constructor(private listOfValuesService: ListOfValueService, private route: ActivatedRoute) {}

  ngOnInit(): void {
   
    this.valueId = this.state.valueId;
      this.listOfValuesService.getChoicesOfListOfValueId(this.valueId).subscribe(data=>{
        this.formData=data;
      });
    this.listOfValuesService.getListOfValues().subscribe(data => {
      data.forEach(item => {
        console.log(item.values); 
        this.ismultiple = item.multiple; 
      });
    });
  }


  toggleOptions() {
    this.showOptions = !this.showOptions;
  }

  selectItem(item: any) {
    this.selectedItem = item;
    this.onChange(item);
    this.onTouched();
    this.showOptions = false;
    this.selectionChange.emit(item);
  }

  handleKeyPress(event: KeyboardEvent) {
    if (event.key === 'Enter' && this.showOptions && this.selectedItem) {
      this.selectItem(this.selectedItem);
    }
  }

  writeValue(value: any): void {
    this.selectedItem = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {}

  ngOnDestroy() {
    if (this.globalClickListener) {
      this.globalClickListener();
    }
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  formatSelectValue(item: any): string {
    return `${item.key} `;
  }

  tests = ['key1', 'key2'];
}
