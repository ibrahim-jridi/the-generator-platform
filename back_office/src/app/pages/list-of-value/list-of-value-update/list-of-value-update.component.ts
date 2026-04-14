import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { cH } from '@fullcalendar/core/internal-common';
import { ListOfValue } from 'src/app/shared/models/listOfValue.model';
import { ListOfValueService } from 'src/app/shared/services/list-of-value.service';

@Component({
  selector: 'app-list-of-value-update',
  templateUrl: './list-of-value-update.component.html',
  styleUrl: './list-of-value-update.component.scss'
})
export class ListOfValueUpdateComponent implements OnInit{

  formData: ListOfValue;
  templateForm:any;
  valueForm: FormGroup;
  id:any
  public state = window.history.state;

  constructor(private listofvalueservice: ListOfValueService, private fb: FormBuilder, private route: ActivatedRoute) { 
      this.valueForm = this.fb.group({
        id: [{ value: '', disabled: true } , Validators.required],
        label: [{ value: '', disabled: false } , Validators.required],
        description: [{ value: '', disabled: false }, Validators.required],
        ismultiple: [false],
        isrequired:[],
        placeholder: ['', [Validators.required]],
        values: this.fb.array([this.createListOfValueFormGroup()], {
          validators: [
            (formArr: FormArray) => {
              const met = new Set<string>();
              return formArr.controls.some(({ value: { key, value } }) => {
                const val = `${key}|${value}`;
                if (met.has(val)) return true;
                met.add(val);
                return false;
              })
                ? { hasDuplicates: true }
               : null;
            },
          ],
        })
      });
    }



    
  ngOnInit(): void {

       this.id = this.state.valueId;
      if (this.id) {
        this.listofvalueservice.getListOfValuesById(this.id).subscribe(data => {
          this.formData = data;
          this.valueForm.patchValue({
            id:this.formData.id,
            label: this.formData.label,
            description: this.formData.description,
            ismultiple: this.formData.multiple,
            isrequired: this.formData.required,
            placeholder: this.formData.placeHolder
          });
          const valuesArray = this.valueForm.get('values') as FormArray;
          valuesArray.clear(); 
          this.formData.choices.forEach(choice => {
            valuesArray.push(this.fb.group({
              id:[choice.id],
              key: [choice.key],
              value: [choice.value, Validators.required]
            }));
          });
        });
      }

    

  }


  private createListOfValueFormGroup(): FormGroup {
    return this.fb.group({
      key: [''],
      value: ['', [Validators.required]],
    });
  }
  get values(): FormArray {
    return this.valueForm.get('values') as FormArray;
  }

  public addChoices(): void {

    let array = this.values.getRawValue();
    const duplicateObjects = this.findDuplicates(array, ['key', 'value']);
    if (duplicateObjects.length > 0) {
    } else {
      this.values.push(this.createListOfValueFormGroup());
    }
  }

  findDuplicates(arr, fields) {
    const seen = new Map();
    return arr.filter((item) => {
      const key = fields.map((field) => item[field]).join('|');
      if (seen.has(key)) {
        return true;
      }
      seen.set(key, true);
      return false;
    });
  }

  public onChangeInput(event: Event,index: number): void {

    const inputElement = event.target as HTMLInputElement;
    const currentValueGroup = this.values.at(index);

    currentValueGroup?.patchValue({
      value: inputElement.value,
      key: this.transformToUppercase(inputElement.value)
    });
  }

  transformToUppercase(value: string): string {
    if (!value) return '';

    value = value.replace(/[éèà]/gi, function(match) {
      switch(match.toLowerCase()) {
          case 'é':
              return 'e';
          case 'è':
              return 'e';
          case 'à':
              return 'a';
          default:
              return match;
      }
    });

    const words = value.trim().split(/["'{}[\](_@ ,;: -&<>=#|`]|\+!/);
    const camelCaseValue = words.map((word, index) => {
        if (index === 0) {
            return word.toLowerCase(); // Lowercase the first word
        } else {
            return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase(); // Capitalize subsequent words
        }
    }).join('');
    return camelCaseValue;

  }

  public removeChoices(index: number): void {
    this.values.removeAt(index);
  }

  public onSubmit(): void {
    const formData: ListOfValue = {
      id: this.valueForm.get('id')?.value || '',
      label: this.valueForm.get('label')?.value || '',
      description: this.valueForm.get('description')?.value || '',
      placeHolder: this.valueForm.get('placeholder')?.value || '', 
      createdDate: new Date().toISOString(), 
      updatedDate: new Date().toISOString(),
      createdBy: '', 
      multiple: this.valueForm.get('ismultiple')?.value || 'false',
      required: this.valueForm.get('isrequired')?.value || 'false',
      choices: (this.valueForm.get('values') as FormArray).controls.map((group: FormGroup) => ({
        id: group.get('id')?.value || '',  
        key: group.get('key')?.value || '',
        value: group.get('value')?.value || ''
      }))
  };


  this.listofvalueservice.updateListOfValue(formData,this.id).subscribe({
      next: () => {
        alert('List Of Value updated successful');
      },
      error: (error: any) => {
        alert('List Of Value updated failed');
      }
  });
  }
}
