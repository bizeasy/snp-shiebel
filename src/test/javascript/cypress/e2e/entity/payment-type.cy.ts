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

describe('PaymentType e2e test', () => {
  const paymentTypePageUrl = '/payment-type';
  let username: string;
  let password: string;
  const paymentTypeSample = { name: 'fatal alongside' };

  let paymentType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-types/${paymentType.id}`,
      }).then(() => {
        paymentType = undefined;
      });
    }
  });

  it('PaymentTypes menu should load PaymentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentType').should('exist');
    cy.location('pathname').should('eq', paymentTypePageUrl);
  });

  describe('PaymentType page', () => {
    it('should have translated page title', () => {
      cy.visit(paymentTypePageUrl);
      cy.getEntityHeading('PaymentType').should('not.contain', 'snpApp.paymentType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${paymentTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PaymentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-types',
          body: paymentTypeSample,
        }).then(({ body }) => {
          paymentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [paymentType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentTypePageUrl);
      });

      it('edit button click should load edit PaymentType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentTypePageUrl);
      });

      it('edit button click should load edit PaymentType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentTypePageUrl);
      });

      it('last delete button click should delete instance of PaymentType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentTypePageUrl);

        paymentType = undefined;
      });
    });
  });

  describe('new PaymentType page', () => {
    beforeEach(() => {
      cy.visit(paymentTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentType');
    });

    it('should create an instance of PaymentType', () => {
      cy.get(`[data-cy="name"]`).type('really');
      cy.get(`[data-cy="name"]`).should('have.value', 'really');

      cy.get(`[data-cy="description"]`).type('aha what');
      cy.get(`[data-cy="description"]`).should('have.value', 'aha what');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        paymentType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', paymentTypePageUrl);
    });
  });
});
