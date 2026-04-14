import { Component, Input, Output, EventEmitter, ElementRef, Renderer2, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true
    }
  ]
})
export class SelectComponent implements ControlValueAccessor {
  @Input() items: any[] = [];
  @Input() displayFn: (item: any) => string;
  @Input() placeholder: string;
  @Output() selectionChange = new EventEmitter<any>();
  @Input() ngClass: string = '';

  showOptions = false;
  selectedItem: any;

  private onChange: any = () => { };
  private onTouched: any = () => { };
  private globalClickListener: () => void;

  constructor(private elementRef: ElementRef, private renderer: Renderer2) {
    this.globalClickListener = this.renderer.listen('document', 'click', this.handleGlobalClick.bind(this));
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

  private handleGlobalClick(event: Event) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.showOptions = false;
    }
  }

  // Implementation of ControlValueAccessor interface
  writeValue(value: any): void {
    this.selectedItem = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    // Optional implementation if needed
  }

  ngOnDestroy() {
    if (this.globalClickListener) {
      this.globalClickListener();
    }
  }
}
