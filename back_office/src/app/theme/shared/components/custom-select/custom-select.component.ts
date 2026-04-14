import {Component, Input, OnInit, forwardRef, EventEmitter, Output} from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-custom-select',
  templateUrl: './custom-select.component.html',
  styleUrls: ['./custom-select.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CustomSelectComponent),
      multi: true
    }
  ]
})
export class CustomSelectComponent implements OnInit, ControlValueAccessor {

  @Input() items: any[] = []; // Les items à afficher dans le select
  @Input() bindValue: string = 'id'; // La propriété à utiliser comme valeur
  @Input() bindLabel: string = 'name'; // La propriété à utiliser comme label
  @Input() multiple: boolean = true; // Pour définir si le select est multiple ou non
  @Input() placeholder: string = 'Select items'; // Placeholder pour le select
  @Input() control: FormControl = new FormControl(); // FormControl à utiliser
  @Input() set disabled(value: boolean) {
    this.isDisabled = value;
    if (value) {
      this.control.disable({ emitEvent: false });
    } else {
      this.control.enable({ emitEvent: false });
    }
  }
  get disabled(): boolean {
    return this.isDisabled;
  }
  @Output() selectionChange = new EventEmitter<any>();
  @Input() showCheckbox: boolean = false;
  @Input() checkIfDisabled: (item: any) => boolean;
  @Input() clearable: boolean = false;
  private isDisabled: boolean = false;
  public value: any;
  public onChange: any = (value: any) => {};
  public onTouched: any = () => { };

  ngOnInit() {
    if (!this.multiple && this.items.length) {
      this.control.setValue(this.items[0][this.bindValue]);
    }
  }

  onSelectAll() {
    if (this.multiple) {
      this.control.setValue(this.items.map(item => item[this.bindValue]));
      this.onValueChange(this.control.value); // Propager les changements
    }
  }

  onClearAll() {
    this.control.setValue([]);
    this.onValueChange(this.control.value); // Propager les changements
  }

  writeValue(value: any): void {
    this.value = value;
    this.control.setValue(value || (this.multiple ? [] : null), { emitEvent: false });
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    //this.disabled= isDisabled; not need to implement by asghaier
  }

  private isArrayOfObjects(value: any): boolean {
    if (Array.isArray(value)) {
      return value.every(item => typeof item === 'object' && item !== null);
    }
    return false;
  }

  onValueChange(event: any) {
    this.value = event;
    if (this.multiple && this.isArrayOfObjects(this.value)) {
      this.value = this.value.map(item => item[this.bindValue]);
    }
    this.onChange(this.value);
    this.selectionChange.emit(this.value);
  }
}
