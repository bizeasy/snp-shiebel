import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ReturnItemBillingDetail } from './return-item-billing-detail';

describe('ReturnItemBilling Management Detail Component', () => {
  let comp: ReturnItemBillingDetail;
  let fixture: ComponentFixture<ReturnItemBillingDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./return-item-billing-detail').then(m => m.ReturnItemBillingDetail),
              resolve: { returnItemBilling: () => of({ id: 25080 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReturnItemBillingDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load returnItemBilling on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReturnItemBillingDetail);

      // THEN
      expect(instance.returnItemBilling()).toEqual(expect.objectContaining({ id: 25080 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
