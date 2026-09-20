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

describe('PaymentMethodType e2e test', () => {
  const paymentMethodTypePageUrl = '/payment-method-type';
  let username: string;
  let password: string;
  const paymentMethodTypeSample = { name: 'apropos throughout' };

  let paymentMethodType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-method-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-method-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-method-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentMethodType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-method-types/${paymentMethodType.id}`,
      }).then(() => {
        paymentMethodType = undefined;
      });
    }
  });

  it('PaymentMethodTypes menu should load PaymentMethodTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-method-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentMethodType').should('exist');
    cy.location('pathname').should('eq', paymentMethodTypePageUrl);
  });

  describe('PaymentMethodType page', () => {
    it('should have translated page title', () => {
      cy.visit(paymentMethodTypePageUrl);
      cy.getEntityHeading('PaymentMethodType').should('not.contain', 'snpApp.paymentMethodType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentMethodTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentMethodType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${paymentMethodTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PaymentMethodType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-method-types',
          body: paymentMethodTypeSample,
        }).then(({ body }) => {
          paymentMethodType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-method-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [paymentMethodType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentMethodTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentMethodType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentMethodType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodTypePageUrl);
      });

      it('edit button click should load edit PaymentMethodType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentMethodType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodTypePageUrl);
      });

      it('edit button click should load edit PaymentMethodType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentMethodType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodTypePageUrl);
      });

      it('last delete button click should delete instance of PaymentMethodType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentMethodType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentMethodTypePageUrl);

        paymentMethodType = undefined;
      });
    });
  });

  describe('new PaymentMethodType page', () => {
    beforeEach(() => {
      cy.visit(paymentMethodTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentMethodType');
    });

    it('should create an instance of PaymentMethodType', () => {
      cy.get(`[data-cy="name"]`).type('pish gracefully potential');
      cy.get(`[data-cy="name"]`).should('have.value', 'pish gracefully potential');

      cy.get(`[data-cy="description"]`).type('yawningly in');
      cy.get(`[data-cy="description"]`).should('have.value', 'yawningly in');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        paymentMethodType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', paymentMethodTypePageUrl);
    });
  });
});
