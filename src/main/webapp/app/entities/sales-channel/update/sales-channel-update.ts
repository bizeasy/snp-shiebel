import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ISalesChannel } from '../sales-channel.model';
import { SalesChannelService } from '../service/sales-channel.service';

import { SalesChannelFormGroup, SalesChannelFormService } from './sales-channel-form.service';

@Component({
  selector: 'sys-sales-channel-update',
  templateUrl: './sales-channel-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class SalesChannelUpdate implements OnInit {
  readonly isSaving = signal(false);
  salesChannel: ISalesChannel | null = null;

  protected salesChannelService = inject(SalesChannelService);
  protected salesChannelFormService = inject(SalesChannelFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SalesChannelFormGroup = this.salesChannelFormService.createSalesChannelFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salesChannel }) => {
      this.salesChannel = salesChannel;
      if (salesChannel) {
        this.updateForm(salesChannel);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const salesChannel = this.salesChannelFormService.getSalesChannel(this.editForm);
    if (salesChannel.id === null) {
      this.subscribeToSaveResponse(this.salesChannelService.create(salesChannel));
    } else {
      this.subscribeToSaveResponse(this.salesChannelService.update(salesChannel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ISalesChannel | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(salesChannel: ISalesChannel): void {
    this.salesChannel = salesChannel;
    this.salesChannelFormService.resetForm(this.editForm, salesChannel);
  }
}
