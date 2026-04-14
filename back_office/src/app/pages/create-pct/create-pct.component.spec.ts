import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePctComponent } from './create-pct.component';

describe('CreatePctComponent', () => {
  let component: CreatePctComponent;
  let fixture: ComponentFixture<CreatePctComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreatePctComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreatePctComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
