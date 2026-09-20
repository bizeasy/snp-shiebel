import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { of } from 'rxjs';

import { QuoteRoleService } from '../service/quote-role.service';

import { QuoteRoleDeleteDialog } from './quote-role-delete-dialog';

describe('QuoteRole Management Delete Component', () => {
  let comp: QuoteRoleDeleteDialog;
  let fixture: ComponentFixture<QuoteRoleDeleteDialog>;
  let service: QuoteRoleService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NgbActiveModal],
    });
    fixture = TestBed.createComponent(QuoteRoleDeleteDialog);
    comp = fixture.componentInstance;
    service = TestBed.inject(QuoteRoleService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', () => {
      // GIVEN
      vi.spyOn(service, 'delete').mockReturnValue(of(undefined));
      vi.spyOn(mockActiveModal, 'close');

      // WHEN
      comp.confirmDelete(123);

      // THEN
      expect(service.delete).toHaveBeenCalledWith(123);
      expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
    });
  });

  describe('cancel', () => {
    it('should not call delete service on cancel', () => {
      // GIVEN
      vi.spyOn(service, 'delete');
      vi.spyOn(mockActiveModal, 'close');
      vi.spyOn(mockActiveModal, 'dismiss');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
