import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InternalUserViewComponent } from './internal-user-view.component';

describe('InternalUserViewComponent', () => {
  let component: InternalUserViewComponent;
  let fixture: ComponentFixture<InternalUserViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InternalUserViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InternalUserViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
