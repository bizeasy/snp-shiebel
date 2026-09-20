import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CustRequestItemAttachmentDetail } from './cust-request-item-attachment-detail';

describe('CustRequestItemAttachment Management Detail Component', () => {
  let comp: CustRequestItemAttachmentDetail;
  let fixture: ComponentFixture<CustRequestItemAttachmentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cust-request-item-attachment-detail').then(m => m.CustRequestItemAttachmentDetail),
              resolve: { custRequestItemAttachment: () => of({ id: 342 }) },
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
    fixture = TestBed.createComponent(CustRequestItemAttachmentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load custRequestItemAttachment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CustRequestItemAttachmentDetail);

      // THEN
      expect(instance.custRequestItemAttachment()).toEqual(expect.objectContaining({ id: 342 }));
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
