import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListOfValueUpdateComponent } from './list-of-value-update.component';

describe('ListOfValueUpdateComponent', () => {
  let component: ListOfValueUpdateComponent;
  let fixture: ComponentFixture<ListOfValueUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListOfValueUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListOfValueUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
