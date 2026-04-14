import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsidiaryPctComponent } from './subsidiary-pct.component';

describe('SubsidiaryPctComponent', () => {
  let component: SubsidiaryPctComponent;
  let fixture: ComponentFixture<SubsidiaryPctComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubsidiaryPctComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SubsidiaryPctComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
