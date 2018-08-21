import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HostOverviewComponent } from './host-overview.component';

describe('HostOverviewComponent', () => {
  let component: HostOverviewComponent;
  let fixture: ComponentFixture<HostOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HostOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HostOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
