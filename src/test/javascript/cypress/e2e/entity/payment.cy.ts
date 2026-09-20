import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Payment e2e test', () => {
  const paymentPageUrl = '/payment';
  let username: string;
  let password: string;
  const paymentSample = { amount: 17745.12 };

  let payment;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (payment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payments/${payment.id}`,
      }).then(() => {
        payment = undefined;
      });
    }
  });

  it('Payments menu should load Payments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Payment').should('exist');
    cy.location('pathname').should('eq', paymentPageUrl);
  });

  describe('Payment page', () => {
    it('should have translated page title', () => {
      cy.visit(paymentPageUrl);
      cy.getEntityHeading('Payment').should('not.contain', 'snpApp.payment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Payment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${paymentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payments',
          body: paymentSample,
        }).then(({ body }) => {
          payment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payments?page=0&size=20>; rel="last",<http://localhost/api/payments?page=0&size=20>; rel="first"',
              },
              body: [payment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Payment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('payment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentPageUrl);
      });

      it('edit button click should load edit Payment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentPageUrl);
      });

      it('edit button click should load edit Payment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentPageUrl);
      });

      it('last delete button click should delete instance of Payment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('payment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentPageUrl);

        payment = undefined;
      });
    });
  });

  describe('new Payment page', () => {
    beforeEach(() => {
      cy.visit(paymentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Payment');
    });

    it('should create an instance of Payment', () => {
      cy.get(`[data-cy="paymentRefNumber"]`).type('until');
      cy.get(`[data-cy="paymentRefNumber"]`).should('have.value', 'until');

      cy.get(`[data-cy="effectiveDate"]`).type('2023-12-14T09:35');
      cy.get(`[data-cy="effectiveDate"]`).blur();
      cy.get(`[data-cy="effectiveDate"]`).should('have.value', '2023-12-14T09:35');

      cy.get(`[data-cy="paymentDate"]`).type('2023-12-14T22:32');
      cy.get(`[data-cy="paymentDate"]`).blur();
      cy.get(`[data-cy="paymentDate"]`).should('have.value', '2023-12-14T22:32');

      cy.get(`[data-cy="amount"]`).type('3188.66');
      cy.get(`[data-cy="amount"]`).should('have.value', '3188.66');

      cy.get(`[data-cy="actualAmount"]`).type('5963.96');
      cy.get(`[data-cy="actualAmount"]`).should('have.value', '5963.96');

      cy.get(`[data-cy="paymentStatus"]`).type('meh altruistic');
      cy.get(`[data-cy="paymentStatus"]`).should('have.value', 'meh altruistic');

      cy.get(`[data-cy="mihpayId"]`).type('vice instance');
      cy.get(`[data-cy="mihpayId"]`).should('have.value', 'vice instance');

      cy.get(`[data-cy="txnId"]`).type('reluctantly gently than');
      cy.get(`[data-cy="txnId"]`).should('have.value', 'reluctantly gently than');

      cy.get(`[data-cy="email"]`).type('Sarai_Schulist52@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Sarai_Schulist52@gmail.com');

      cy.get(`[data-cy="phone"]`).type('1-953-757-0351 x3202');
      cy.get(`[data-cy="phone"]`).should('have.value', '1-953-757-0351 x3202');

      cy.get(`[data-cy="productInfo"]`).type('consign horst downright');
      cy.get(`[data-cy="productInfo"]`).should('have.value', 'consign horst downright');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        payment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', paymentPageUrl);
    });
  });
});
