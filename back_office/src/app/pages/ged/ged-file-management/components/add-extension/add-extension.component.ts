import {AfterViewInit, Component, Inject, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { SizeConfig } from '../data-table/data-table.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-add-extension',
  templateUrl: './add-extension.component.html',
  styleUrls: ['./add-extension.component.scss']
})
export class AddExtensionComponent implements OnInit  , AfterViewInit{
  formGroup!: FormGroup;
  @Input() sizeConfig : SizeConfig
  title : string;
  modeView: string;
  unit: string[] = ["Octet", "Kilo Octet", "Mega Octet", "Giga Octet"];
  constructor(private formBuilder: FormBuilder, private translate: TranslateService,
              public dialogRef: MatDialogRef<AddExtensionComponent>,
              @Inject(MAT_DIALOG_DATA) public data:  any
              ) {
    this.sizeConfig = this.data.sizeConfig;
    this.title = this.data.title;

  }

  onNoClick(): void {

  }
  closeDialog(value){
    this.dialogRef.close(value);
  }

  ngOnInit() {
    this.createForm()
  }


  createForm() {
    let unit;
    let size;
    if (this.sizeConfig.maxSize < 1000) {
      unit = "Octet";
      size = this.sizeConfig.maxSize;
    } else  if (this.sizeConfig.maxSize < 1000000) {
      unit = "Kilo Octet";
      size = this.sizeConfig.maxSize / 1000;
    } else  if (this.sizeConfig.maxSize < 1000000000) {
      unit = "Mega Octet";
      size = this.sizeConfig.maxSize / 1000000;
    } else  if (this.sizeConfig.maxSize >= 1000000000) {
      unit = "Giga Octet";
      size = this.sizeConfig.maxSize / 1000000000;
  }
    this.formGroup = this.formBuilder.group({
      extension: [ this.sizeConfig && this.sizeConfig.extension ? this.sizeConfig.extension : null , Validators.required],
      maxSize: [this.sizeConfig && this.sizeConfig.maxSize ? size : null],
      unit: [this.sizeConfig ? unit : null],

    });
  }

//TODO onSubmit file extension
  onSubmit() {}


  ngAfterViewInit(): void {
    this.createForm()
  }

  ngOnDestroy(): void {
    this.dialogRef.close(true);
  }
}
