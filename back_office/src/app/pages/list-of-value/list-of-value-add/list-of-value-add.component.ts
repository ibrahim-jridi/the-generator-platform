import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Choice } from 'src/app/shared/models/choice.model';
import { ListOfValue } from 'src/app/shared/models/listOfValue.model';
import { ListOfValueService } from 'src/app/shared/services/list-of-value.service';

@Component({
  selector: 'app-list-of-value-add',
  templateUrl: './list-of-value-add.component.html',
  styleUrls: ['./list-of-value-add.component.scss']
})
export class ListOfValueAddComponent implements OnInit {
  protected valueForm: FormGroup;
  public formData:ListOfValue=new ListOfValue();

  constructor(private fb: FormBuilder, private sharedDataService:ListOfValueService) {
    this.ListOfValueForm();
  }

  public ngOnInit(): void { }

  private ListOfValueForm(): void {
    this.valueForm = this.fb.group({
     // id: ['', [Validators.required]],
      label: ['', [Validators.required]],
      description: ['', [Validators.required]],
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
    console.log(this.valueForm.value);
   // this.valueForm.setValidators(this.uniqueSkillValidator());
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

  public removeChoices(index: number): void {
    this.values.removeAt(index);
  }

  public onChangeInput(event: Event,index: number): void {

    const inputElement = event.target as HTMLInputElement;
    const currentValueGroup = this.values.at(index);

    currentValueGroup?.patchValue({
      value: inputElement.value,
      key: this.transformToUppercase(inputElement.value)
    });
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
      choices:  this.values.controls.map((group) => ({
          key: group.get('key')?.value || '',
          value: group.get('value')?.value || ''
      }))
  };

  this.sharedDataService.saveListOfValue(formData).subscribe({
      next: () => {
        alert('List Of Value submission successful');
        this.valueForm.reset();
      },
      error: (error: any) => {
        alert('List Of Value submission failed');
      }
  });
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


}
