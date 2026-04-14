import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateGrossisteComponent } from './create-grossiste.component';

describe('CreateGrossisteComponent', () => {
  let component: CreateGrossisteComponent;
  let fixture: ComponentFixture<CreateGrossisteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateGrossisteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateGrossisteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
