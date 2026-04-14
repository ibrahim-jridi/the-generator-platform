import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListOfValueComponent } from './list-of-value.component';

describe('ListOfValueComponent', () => {
  let component: ListOfValueComponent;
  let fixture: ComponentFixture<ListOfValueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListOfValueComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListOfValueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
