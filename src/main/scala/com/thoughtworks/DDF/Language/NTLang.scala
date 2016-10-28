package com.thoughtworks.DDF.Language

import scalaz.{Forall, NaturalTransformation}

trait NTLang[Info[_], Repr[_], F[_]] extends Lang[Info, F] {
  def base: Lang[Info, Repr]

  def NTF: NaturalTransformation[Repr, F]

  override def K[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.K)

  override def ltD = NTF(base.ltD)

  override def App[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.App)

  override implicit def sumInfo[A, B](implicit ai: Info[A], bi: Info[B]) = base.sumInfo

  override def sumLeftInfo[A, B] = base.sumLeftInfo

  override def sumRightInfo[A, B] = base.sumRightInfo

  override def scanRight[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.scanRight)

  override def W[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.W)

  override def multD = NTF(base.multD)

  override def scanLeft[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.scanLeft)

  override def left[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.left)

  override def right[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.right)

  override def sumMatch[A, B, C](implicit ai: Info[A], bi: Info[B], ci: Info[C]) = NTF(base.sumMatch)

  override def sigD = NTF(base.sigD)

  override def Let[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.Let)

  override implicit def arrowInfo[A, B](implicit ai: Info[A], bi: Info[B]) = base.arrowInfo

  override def arrowDomainInfo[A, B] = base.arrowDomainInfo

  override def arrowRangeInfo[A, B] = base.arrowRangeInfo

  override def mkUnit = NTF(base.mkUnit)

  override implicit def optionInfo[A](implicit ai: Info[A]) = base.optionInfo

  override def optionElmInfo[A] = base.optionElmInfo

  override def listZip[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.listZip)

  override implicit def listInfo[A](implicit ai: Info[A]) = base.listInfo

  override def listElmInfo[A] = base.listElmInfo

  override def C[A, B, C](implicit ai: Info[A], bi: Info[B], ci: Info[C]) = NTF(base.C)

  override def foldLeft[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.foldLeft)

  override def plusD = NTF(base.plusD)

  override def divD = NTF(base.divD)

  override def none[A](implicit ai: Info[A]) = NTF(base.none)

  override def some[A](implicit ai: Info[A]) = NTF(base.some)

  override def optionMatch[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.optionMatch)

  override def reverse[A](implicit ai: Info[A]) = NTF(base.reverse)

  override def listMap[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.listMap)

  override def foldRight[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.foldRight)

  override def uncurry[A, B, C](implicit ai: Info[A], bi: Info[B], ci: Info[C]) = NTF(base.uncurry)

  override def curry[A, B, C](implicit ai: Info[A], bi: Info[B], ci: Info[C]) = NTF(base.curry)

  override implicit def productInfo[A, B](implicit ai: Info[A], bi: Info[B]) = base.productInfo

  override def productZerothInfo[A, B] = base.productZerothInfo

  override def productFirstInfo[A, B] = base.productFirstInfo

  override def litD = d => NTF(base.litD(d))

  override def litB = b => NTF(base.litB(b))

  override def ite[A](implicit ai: Info[A]) = NTF(base.ite)

  override def sumComm[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.sumComm)

  override def sumAssocLR[A, B, C](implicit ai: Info[A], bi: Info[B], ci: Info[C]) = NTF(base.sumAssocLR)

  override def sumAssocRL[A, B, C](implicit ai: Info[A], bi: Info[B], ci: Info[C]) = NTF(base.sumAssocRL)

  override implicit def unitInfo = base.unitInfo

  override def S[A, B, C](implicit ai: Info[A], bi: Info[B], ci: Info[C]) = NTF(base.S)

  override def expD = NTF(base.expD)

  override implicit def boolInfo = base.boolInfo

  override def B[A, B, C](implicit ai: Info[A], bi: Info[B], ci: Info[C]) = NTF(base.B)

  override def I[A](implicit ai: Info[A]) = NTF(base.I)

  override def mkProduct[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.mkProduct)

  override def zeroth[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.zeroth)

  override def first[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.first)

  override def Y[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.Y)

  override def nil[A](implicit ai: Info[A]) = NTF(base.nil)

  override def cons[A](implicit ai: Info[A]) = NTF(base.cons)

  override def listMatch[A, B](implicit ai: Info[A], bi: Info[B]) = NTF(base.listMatch)

  override implicit def doubleInfo = base.doubleInfo
}

object NTLang {
  implicit def apply[Info[_], Repr[_], F[_]](implicit
                                             l: Lang[Info, Repr],
                                             n: NaturalTransformation[Repr, F],
                                             ap: Forall[Lambda[A => Forall[Lambda[B => F[A => B] => F[A] => F[B]]]]],
                                             r: NaturalTransformation[F, Info]): NTLang[Info, Repr, F] =
    new NTLang[Info, Repr, F] {
      override def base: Lang[Info, Repr] = l

      override def NTF: NaturalTransformation[Repr, F] = n

      override def app[A, B] = ap.apply[A].apply[B]

      override def reprInfo[A] = r.apply[A]
  }
}