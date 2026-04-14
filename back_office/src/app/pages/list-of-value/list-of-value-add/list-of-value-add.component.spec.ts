import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListOfValueAddComponent } from './list-of-value-add.component';

describe('ListOfValueAddComponent', () => {
  let component: ListOfValueAddComponent;
  let fixture: ComponentFixture<ListOfValueAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListOfValueAddComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListOfValueAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
