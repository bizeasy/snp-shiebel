import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CustRequestAttachmentDetail } from './cust-request-attachment-detail';

describe('CustRequestAttachment Management Detail Component', () => {
  let comp: CustRequestAttachmentDetail;
  let fixture: ComponentFixture<CustRequestAttachmentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cust-request-attachment-detail').then(m => m.CustRequestAttachmentDetail),
              resolve: { custRequestAttachment: () => of({ id: 31720 }) },
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
    fixture = TestBed.createComponent(CustRequestAttachmentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load custRequestAttachment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CustRequestAttachmentDetail);

      // THEN
      expect(instance.custRequestAttachment()).toEqual(expect.objectContaining({ id: 31720 }));
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
